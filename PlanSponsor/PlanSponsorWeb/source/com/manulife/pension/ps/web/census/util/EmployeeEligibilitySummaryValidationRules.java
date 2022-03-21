package com.manulife.pension.ps.web.census.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.validator.ValidationError;

/**
 * The form field validation rules for the Employee Eligibility summary
 * 
 * @author patuadr
 *
 */
public class EmployeeEligibilitySummaryValidationRules {
    public static String OPT_OUT = "optOut";
    public static String ELIGIBILITY_DATE = "eligibilityDate";
    public static String ELIGIBLE_TO_ENROLL = "eligibleToEnroll";
    public static String DEFAULT_DEFERRAL = "contributionPct"; 
    public static String MISSING_BIRTH_DATE = "missingBirthDate";
    
    private static Date MinimumED = new GregorianCalendar(1900, 0, 1).getTime();

    
    /**
     * Combined edits done: #63.1, #91.1, #92.1, #93.1, 
     * 
     * @param errors
     * @param element
     * @return
     */
    public static boolean validateOptOutIndicator(List<ValidationError> errors, EmployeeSummaryDetails element) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(1);
        numberFormatter.setMaximumFractionDigits(2);

        boolean noError = true;
        // AEE1  removed combined edits #63.1, #91.1, #92.1, #93.1 - keep method in case of future validation rules
        /* double beforeTaxDefPer = 0;
        double beforeTaxDefAmt = 0;
        double afterTaxDefPct = 0;
        double afterTaxDefAmt = 0;        
        Number number = null;
        boolean isOptOut = element.getAutoEnrollOptOutInd() != null && 
                            "Y".equalsIgnoreCase(element.getAutoEnrollOptOutInd());
        
        if(element.getContributionPct() != null && !"".equals(element.getContributionPct().trim())) {
            ParsePosition pos = new ParsePosition(0);            
            number = numberFormatter.parse(element.getContributionPct(), pos);
            
            if(pos.getIndex() != 0 && pos.getIndex() == element.getContributionPct().length() && number != null) {  
                    beforeTaxDefPer = number.doubleValue(); 
            } 
        }
        
        if(element.getBeforeTaxDeferralAmt() != null && !"".equals(element.getBeforeTaxDeferralAmt().trim())) {
            try {
                beforeTaxDefAmt = numberFormatter.parse(element.getBeforeTaxDeferralAmt()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                beforeTaxDefAmt = 0;
            }   
        }
        
        if(element.getAfterTaxDeferralPct() != null && !"".equals(element.getAfterTaxDeferralPct().trim())) {
            try {
                afterTaxDefPct = numberFormatter.parse(element.getAfterTaxDeferralPct()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                afterTaxDefPct = 0;
            }   
        }
        
        if(element.getAfterTaxDeferralAmt() != null && !"".equals(element.getAfterTaxDeferralAmt().trim())) {
            try {
                afterTaxDefAmt = numberFormatter.parse(element.getAfterTaxDeferralAmt()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                afterTaxDefAmt = 0;
            }   
        }       
        
        if(isOptOut && beforeTaxDefPer > 0) {
            errors.add(new ValidationError(OPT_OUT, CensusErrorCodes.EligibilityOptOutBeforeTaxDefPer, getEmployeeName(element)));
            element.setOptOutStatus(EmployeeSummaryDetails.ERROR);   
            noError = false;            
        }        
        
        if(isOptOut && beforeTaxDefAmt > 0) {
            errors.add(new ValidationError(OPT_OUT, CensusErrorCodes.EligibilityBeforeTaxDeferralAmt, getEmployeeName(element)));
            element.setOptOutStatus(EmployeeSummaryDetails.ERROR);   
            noError = false;            
        }
        
        if(isOptOut && afterTaxDefPct > 0) {
            errors.add(new ValidationError(OPT_OUT, CensusErrorCodes.OptOutDesignatedRothDefPctEligibility, getEmployeeName(element)));
            element.setOptOutStatus(EmployeeSummaryDetails.ERROR);   
            noError = false;            
        } 
        
        if(isOptOut && afterTaxDefAmt > 0) {
            errors.add(new ValidationError(OPT_OUT, CensusErrorCodes.OptOutDesignatedRothDefAmtEligibility, getEmployeeName(element)));
            element.setOptOutStatus(EmployeeSummaryDetails.ERROR);   
            noError = false;            
        } */
                
        return noError;
    }

    /**
     * Combined edits done: #52, #53, #98.1
     * 
     * @param errors
     * @param element
     * @return
     */
    public static boolean validateDeferralPct(List<ValidationError> errors, EmployeeSummaryDetails element) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(1);
        numberFormatter.setMaximumFractionDigits(3);
        boolean noError = true;
        boolean beforeTaxDefPerFormatError = false;
        double beforeTaxDefPer = 0;
        double beforeTaxDefAmt = 0;
        Number number = null;
        
        if(element.getContributionPct() != null && !"".equals(element.getContributionPct().trim())) {
            if(element.getContributionPct().length() > 6) {
                beforeTaxDefPerFormatError = true;
            } else {
                int decimalIndex = element.getContributionPct().lastIndexOf(".");
                if( decimalIndex > 0) {
                    if(element.getContributionPct().substring(decimalIndex+1).length() > 3 ||
                            element.getContributionPct().substring(0, decimalIndex).length() > 3) {
                        beforeTaxDefPerFormatError = true;
                    }
                    
                }
                
                ParsePosition pos = new ParsePosition(0);            
                number = numberFormatter.parse(element.getContributionPct(), pos);
                    
                // If it cannot be parsed at all, index is 0
                // If not everything is parseable than the index stops where the pasing stopped
                if(pos.getIndex() != 0 && pos.getIndex() == element.getContributionPct().length() && number != null) {
                    beforeTaxDefPer = number.doubleValue();
                } else {
                    beforeTaxDefPerFormatError = true;
                }
            }
        }
        
        if(element.getBeforeTaxDeferralAmt() != null && !"".equals(element.getBeforeTaxDeferralAmt().trim())) {
            try {
                beforeTaxDefAmt = numberFormatter.parse(element.getBeforeTaxDeferralAmt()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                beforeTaxDefAmt = 0;
            }   
        }
        
        if(beforeTaxDefPerFormatError) {
            errors.add(new ValidationError(DEFAULT_DEFERRAL, CensusErrorCodes.BeforeTaxDefPerFormatError, getEmployeeName(element)));
            element.setDeferralStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(beforeTaxDefPer < 0) {
            errors.add(new ValidationError(DEFAULT_DEFERRAL, CensusErrorCodes.EligibilityBeforeTaxDefPerLessThanZero, getEmployeeName(element)));
            element.setDeferralStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(beforeTaxDefPer > 100) {
            errors.add(new ValidationError(DEFAULT_DEFERRAL, CensusErrorCodes.EligibilityBeforeTaxDefPerMoreThan100, getEmployeeName(element)));
            element.setDeferralStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(beforeTaxDefPer > 0 && beforeTaxDefAmt > 0) {
            errors.add(new ValidationError(DEFAULT_DEFERRAL, CensusErrorCodes.BeforeTaxDefAndAmtDiscrepancy, getEmployeeName(element)));
            element.setDeferralStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        return noError;
    }

    /**
     * Combined edits done: #$35.1, #36.1, #61.1 
     * 
     * @param errors
     * @param element
     * @return
     */
    public static boolean validateEligibilityDate(List<ValidationError> errors, EmployeeSummaryDetails element, Date ped) {
        boolean noError = true;
        boolean eligibilityDateFormatError = false;
        Date eligibilityDate = null; 
        
        try {
            eligibilityDate = element.getEligibilityDateAsDate();
        } catch (ParseException e) {
            eligibilityDateFormatError = true;
        }
        
        if(eligibilityDateFormatError) {
            errors.add(new ValidationError(ELIGIBILITY_DATE, CensusErrorCodes.EligibilityDateFormatError, getEmployeeName(element)));
            element.setEligibilityDateStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        } else { 
            // Check if eligibility date is before birth date + 5 years  
            if(element.getBirthDate() != null && eligibilityDate != null) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(element.getBirthDate());                
                calendar.roll(Calendar.YEAR, 5);
                
                if(eligibilityDate.before(calendar.getTime())) {
                    errors.add(new ValidationError(ELIGIBILITY_DATE, CensusErrorCodes.EligibilityDateBirthDateEligibilityPage, getEmployeeName(element)));
                    element.setEligibilityDateStatus(EmployeeSummaryDetails.ERROR);
                    noError = false;
                }
            }
            
            // Check if eligibility date is before hire date  
            if(element.getHireDate() != null && eligibilityDate != null) {                
                if(eligibilityDate.before(element.getHireDate())) {
                    errors.add(new ValidationError(ELIGIBILITY_DATE, CensusErrorCodes.EligibilityDateHireDateEligibilityPage, getEmployeeName(element)));
                    element.setEligibilityDateStatus(EmployeeSummaryDetails.ERROR);
                    noError = false;
                }
            }
            
            // Check if eligibility date is before 01/01/1900
            if(eligibilityDate != null && eligibilityDate.before(MinimumED)) {                
                errors.add(new ValidationError(ELIGIBILITY_DATE, CensusErrorCodes.MinimumEligibilityDateEligibilityPage, getEmployeeName(element)));
                element.setEligibilityDateStatus(EmployeeSummaryDetails.ERROR);
                noError = false;
            }
            
            // TODO - needs refactoring - waiting for DFS to be approved and
            // the change to be implemented
            // Check eligibility is before PED  
//            if(ped != null && eligibilityDate != null) {                
//                if(eligibilityDate.before(ped)) {
//                    errors.add(new ValidationError(ELIGIBILITY_DATE, CensusErrorCodes.EligibilityBeforePlanEffectiveDate));
//                    element.setEligibilityDateStatus(EmployeeSummaryDetails.ERROR);
//                    noError = false;
//                }
//            }
        }
        
        return noError;
    }

    /**
     * Combined edits done: #40.1, #94.2, #95.2, #96.2, #97.2, 
     * 
     * @param errors
     * @param element
     * @return
     */
    public static boolean validateEligibilityIndicator(List<ValidationError> errors, EmployeeSummaryDetails element) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(1);
        numberFormatter.setMaximumFractionDigits(2);
        boolean noError = true;
        boolean eligibleIndBlank = element.getEligibleToEnroll() == null ||
            "".equals(element.getEligibleToEnroll());
        boolean eligibleIndNOrBlank = element.getEligibleToEnroll() == null || 
            "".equals(element.getEligibleToEnroll()) || "N".equalsIgnoreCase(element.getEligibleToEnroll());
        boolean eligibleDateBlank = element.getEligibilityDate() == null || 
            "".equals(element.getEligibilityDate().trim()) || "mm/dd/yyyy".equals(element.getEligibilityDate().trim());
        double afterTaxDefPct = 0;
        double afterTaxDefAmt = 0;
        double beforeTaxDefPer = 0;
        double beforeTaxDefAmt = 0;
        
        if(element.getContributionPct() != null && !"".equals(element.getContributionPct().trim())) {
            try {
                beforeTaxDefPer = numberFormatter.parse(element.getContributionPct()).doubleValue();
            } catch (ParseException e) {
                beforeTaxDefPer = 0;
            }   
        }
        
        if(element.getBeforeTaxDeferralAmt() != null && !"".equals(element.getBeforeTaxDeferralAmt().trim())) {
            try {
                beforeTaxDefAmt = numberFormatter.parse(element.getBeforeTaxDeferralAmt()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                beforeTaxDefAmt = 0;
            }   
        }
        
        if(element.getAfterTaxDeferralPct() != null && !"".equals(element.getAfterTaxDeferralPct().trim())) {
            try {
                afterTaxDefPct = numberFormatter.parse(element.getAfterTaxDeferralPct()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                afterTaxDefPct = 0;
            }   
        }
        
        if(element.getAfterTaxDeferralAmt() != null && !"".equals(element.getAfterTaxDeferralAmt().trim())) {
            try {
                afterTaxDefAmt = numberFormatter.parse(element.getAfterTaxDeferralAmt()).doubleValue();
            } catch (ParseException e) {
                // Safe assumption
                afterTaxDefAmt = 0;
            }   
        }
       // removed AEE1 
       /* if(eligibleIndBlank) {
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.InvalidEligibilityInd, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }*/
        
        // Removed in EC combined edits requirement 
        /*if(eligibleIndNOrBlank && !eligibleDateBlank ) {         
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.EligibilityMissingEligibilityInd, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }*/
        
        if(eligibleIndNOrBlank && afterTaxDefPct > 0) {         
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.EligibilityAfterTaxDeferralPct, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(eligibleIndNOrBlank && afterTaxDefAmt > 0) {         
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.EligibilityAfterTaxDeferralAmt, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(eligibleIndNOrBlank && beforeTaxDefAmt > 0) {         
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.EligibilityPageBeforeTaxFlat, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
        
        if(eligibleIndNOrBlank && beforeTaxDefPer > 0) {         
            errors.add(new ValidationError(ELIGIBLE_TO_ENROLL, CensusErrorCodes.EligibilityPageBeforeTaxDef, getEmployeeName(element)));
            element.setEligibilityIndicatorStatus(EmployeeSummaryDetails.ERROR);
            noError = false;
        }
                
        return noError;
    }
    
    /**
     * Returns the employee names trimmed, to be inserted into the error message 
     * 
     * @param employeeDetails
     * @return
     */
    public static Object[] getEmployeeName(EmployeeSummaryDetails employeeDetails) {
        Object[] obj = new Object[1];
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(CensusUtils.processString(employeeDetails.getLastName()));
        buffer.append(", ");
        buffer.append(CensusUtils.processString(employeeDetails.getFirstName()));
        if(employeeDetails.getMiddleInitial() != null && 
           !"".equals(employeeDetails.getMiddleInitial().trim())) {
            buffer.append(" ");
            buffer.append(CensusUtils.processString(employeeDetails.getMiddleInitial()));
        }        
        
        obj[0] = buffer.toString();
        
        return obj;
    }
  
}
