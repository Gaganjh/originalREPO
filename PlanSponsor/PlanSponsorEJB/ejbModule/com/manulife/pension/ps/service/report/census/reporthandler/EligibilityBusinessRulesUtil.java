package com.manulife.pension.ps.service.report.census.reporthandler;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;

/**
 * 
 * @author patuadr
 *
 */
public class EligibilityBusinessRulesUtil {
    public final static String ACTIVE = "Active";
    public final static String YES = "Y";
    public final static String NO = "N";
    
    public final static String BLANK = "";
    public final static String ENROLLED = "Enrolled";
    public final static String NOT_ELIGIBLE = "Not Eligible";
    public final static String PENDING_ELIGIBILITY = "Pending Eligibility";
    public final static String PENDING_ENROLLMENT = "Pending Enrollment";
    public final static String NO_ACCOUNT = "No Account";
    
    public final static String INTERNET = "Internet";
    public final static String PAPER = "Paper";
    public final static String AUTO = "Auto";
    public final static String DEFAULT = "Default";
    
    public final static String INTERNET_DB = "I";
    public final static String PAPER_DB = "P";
    public final static String AUTO_DB = "A";
    public final static String DEFAULT_DB = "D";
    
    /**
     * Utility method that returns next applicable PED starting from today 
     * as per Enrollment Status definiton algorithm defined in the 
     * requirements document for Eligibility
     * 
     * @return
     * @throws SystemException 
     * @throws ApplicationException 
     */
    public static Date getApplicablePlanEntryDate(EmployeeSummaryDetails element, Date nextPED) throws SystemException {
        Date eligibilityDate;        
        Calendar currentCal = new GregorianCalendar();
        Calendar eligibilityCal = new GregorianCalendar();
        currentCal.setTime(new Date());
        
        try {
            eligibilityDate = element.getEligibilityDateAsDate();
        } catch (ParseException e) {
            // It should not happen
            eligibilityDate = null;
        }
        
        if(eligibilityDate == null) {
            return nextPED;
        } else {
            eligibilityCal.setTime(eligibilityDate);
            if(eligibilityCal.compareTo(currentCal) <= 0) {
                return nextPED;
            } else {
                // TODO - Find the range in the list of ranges of Plan Entry dates 
                //(using Plan Entry date and Frequency), 
                // where Previous Plan Entry Date < Eligibility Date < = Next Plan Entry Date
                // Next Plan Entry Date will be Applicable Plan Entry Date for this employee

                return nextPED;
            }
        }        
    }
    
    /**
     * Calculates enrollment status as per Enrollment Status definition algorithm
     * defined in the requirements document for Eligibility
     * 
     * @param element
     * @throws SystemException 
     */
    public static void processEnrollmentStatusAndApplicablePED(EmployeeSummaryDetails element, Date nextPED) throws SystemException {
        Date applicablePED = getApplicablePlanEntryDate(element, nextPED);
        Calendar currentCal = new GregorianCalendar();
        Calendar applicablePEDCal = new GregorianCalendar();
        currentCal.setTime(new Date());
        currentCal.add(Calendar.DAY_OF_MONTH, 45);
        applicablePEDCal.setTime(applicablePED);
        boolean is45DaysPrior = true;
        
        if(currentCal.compareTo(applicablePEDCal) < 0) {
            is45DaysPrior = false;
        }
        
        // Has a JH account
        if(element.isParticipantInd()) {
            element.setEnrollmentStatus(ENROLLED);
            return;
        } 
        
        element.setApplicablePlanEntryDate(applicablePED);
        
        // Does not have a JH account and is not eligible
        if(NO.equalsIgnoreCase(element.getEligibleToEnroll())) {
            element.setEnrollmentStatus(NOT_ELIGIBLE);
            return;
        }
        
        // Does not have a JH account and is eligible
        if(YES.equalsIgnoreCase(element.getEligibleToEnroll())) {
            if(element.isOptOut()) {
                element.setEnrollmentStatus(NO_ACCOUNT);
                return;
            } else {
                if(is45DaysPrior) {                    
                    element.setEnrollmentStatus(PENDING_ELIGIBILITY);
                    return;
                } else {
                    element.setEnrollmentStatus(PENDING_ENROLLMENT);
                    return;
                }                
            }
        }
        
        // Does not have a JH account and ELIGIBILITY is BLANK and
        // OPT-OUT is BLANK and ELIGIBILITY DATE is BLANK
        element.setEnrollmentStatus(BLANK);        
    }
    
    /**
     * Replaces method code with string description 
     * 
     * @param element
     */
    public static void processEnrollmentMethod(EmployeeSummaryDetails element) {        
        if(element.getEnrollmentMethod() == null ||
                "".equals(element.getEnrollmentMethod())) {
            element.setEnrollmentMethod(BLANK);
        } else if(INTERNET_DB.equalsIgnoreCase(element.getEnrollmentMethod())) {
            element.setEnrollmentMethod(INTERNET);
        } else if(PAPER_DB.equalsIgnoreCase(element.getEnrollmentMethod())) {
            element.setEnrollmentMethod(PAPER);
        } else if(AUTO_DB.equalsIgnoreCase(element.getEnrollmentMethod())) {
            element.setEnrollmentMethod(AUTO);
        } else {
            element.setEnrollmentMethod(DEFAULT);
        } 
    }    
    
    /**
     * Calculates deferral percentage as per definition algorithm
     * defined in the requirements document for Eligibility 
     * 
     * @param element
     */
    public static void processDeferralPercentage(EmployeeSummaryDetails element) {  
        if(ENROLLED.equalsIgnoreCase(element.getEnrollmentStatus()) ||
                PENDING_ELIGIBILITY.equalsIgnoreCase(element.getEnrollmentStatus())) {
            element.setContributionPct(element.getBeforeTaxDeferralPct());
        } else if(PENDING_ENROLLMENT.equalsIgnoreCase(element.getEnrollmentStatus())) {
            double beforeTaxPct = 0;
            double beforeTaxAmt = 0;
            double afterTaxPct = 0;
            double afterTaxAmt = 0;
            try {
                beforeTaxPct = Double.parseDouble(element.getBeforeTaxDeferralPct());
                beforeTaxAmt = Double.parseDouble(element.getBeforeTaxDeferralAmt());
                afterTaxPct = Double.parseDouble(element.getAfterTaxDeferralPct());
                afterTaxAmt = Double.parseDouble(element.getAfterTaxDeferralAmt());
            } catch(NumberFormatException ne) {
                // Do nothing, it should never happen, assume 0
            }
            
            if(beforeTaxPct == 0 && beforeTaxAmt == 0 && afterTaxPct == 0 && afterTaxAmt == 0) {
                // TODO - replace with contract default % from CSF
                element.setContributionPct(element.getBeforeTaxDeferralPct());
            } else {
                element.setContributionPct(element.getBeforeTaxDeferralPct());
            }
        } else {
            element.setContributionPct(BLANK);
        }
    }    
}
