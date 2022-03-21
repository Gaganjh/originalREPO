package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;

/**
 * Data Value Object based on the <code>CensusSummaryDetails</code> implementation
 * 
 * It adds utilities for data conversion and implementations for clone(), equals() and hashCode() 
 * needed for processing the form 
 * 
 * @author patuadr
 *
 */
public class EmployeeSummaryDetails implements Details, Serializable, Cloneable {

    private static final long serialVersionUID = 2515645362600978958L;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String ssn;
    private Date birthDate;
    private Date hireDate;
    private String employmentStatus;
    private String profileId;
    private boolean participantInd;
    private String division;
    private Date employmentStatusEffDate;
    private Date enrollmentProcessedDate;
    private String enrollmentMethod;//Internet, Auto, Was Auto Enroll, Default, Paper
    private String enrollmentStatus;
    private String employerDesignatedID;
    private String eligibleToEnroll;
    private String eligibilityDate;
    private String mailingDate;
    private String languageInd;
    private boolean optOut;
    private String autoEnrollOptOutInd;
    private String contributionPct;
    private String beforeTaxDeferralPct;
    private String beforeTaxDeferralAmt;
    private String afterTaxDeferralPct;
    private String afterTaxDeferralAmt;
    private String atEnrollmentBeforeTaxDeferralPct;
    private String atEnrollmentBeforeTaxDeferralAmt;
    private String atEnrollmentAfterTaxDeferralPct;
    private String atEnrollmentAfterTaxDeferralAmt;
    private EmployeeChangeHistoryVO optOutHistory;
    private List<EmployeeChangeHistoryVO> statusChanges;
    private EligibilityWarnings warnings;
    private int employeeAddressStatus;
    private int birthDateStatus =0; // DM addes for compatibility with warning pattern
    
    private int eligibilityIndicatorStatus;
    private int eligibilityDateStatus;
    private int optOutStatus;
    private int deferralStatus;
    private Date applicablePlanEntryDate;        
    public static final int OK = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    
    // added for AEE
    private String contributionStatus;
    private String participantStatusCode;
    private boolean showHistoryAndNameLink;
    private String withdrawalElection90Days;
    private boolean optOutEditable;
    private boolean optedOutNotVested;
    private boolean hasVestedMoneyOnly;
    
    // Added For EC project. -- START --
    private List<String> planEntryDates;
    private List<String> eligibilityData;
    private String compPeriodDateString;
    private String compPeriodChangeDate;
    // Added For EC project. -- END --
      
    public class EligibilityWarnings implements Serializable {

        private static final long serialVersionUID = 2309416675594517288L;
        private boolean indicatorWarning;
        private boolean dateWarning;
        private boolean missingBirthDateWarning;
        private boolean missingEmployeeAddressWarning;
        private boolean missingBirthDateAndEmployeeAddressWarning;
        private String warningContent;
        
        public boolean hasEligibilityIndicatorWarning() {
            return indicatorWarning;
        }
        
        public boolean hasEligibilityDateWarning() {
            return dateWarning;
        }
        
        public boolean hasAllWarnings() {
            return dateWarning && indicatorWarning && missingBirthDateWarning && missingBirthDateAndEmployeeAddressWarning && missingEmployeeAddressWarning;
        }
        
        public boolean hasAnyWarnings() {
            return dateWarning || indicatorWarning ||missingBirthDateWarning ||missingBirthDateAndEmployeeAddressWarning||missingEmployeeAddressWarning;
        }
        
        public String getWarningContent() {
			return warningContent;
		}

		public void setWarningContent(String warningContent) {
			this.warningContent = warningContent;
		}
        
        public void setEligibilityIndWarning() {
            indicatorWarning = true;
        }
        
        public void clearEligibilityIndWarning() {
            indicatorWarning = false;
        }
        
        public void setEligibilityDateWarning() {
            dateWarning = true;
        }
        
        public void clearEligibilityDateWarning() {
            dateWarning = false;
        }
        
        public void setMissingBirthDateWarning () {
        	missingBirthDateWarning = true;
        }
        public void clearMissingBirthDateWarning () {
        	missingBirthDateWarning = false;
        }
        public boolean hasMissingBirthDateWarning () {     	
        	return missingBirthDateWarning;
        }
        public void setMissingEmployeeAddressWarning () {
        	missingEmployeeAddressWarning = true;
        }
		public void setMissingBirthDateAndEmployeeAddressWarning() {
			missingBirthDateAndEmployeeAddressWarning = true;
		}
		public void clearMissingBirthDateAndEmployeeAddressWarning() {
			missingBirthDateAndEmployeeAddressWarning = false;
		}

		public boolean hasMissingBirthDateAndEmployeeAddressWarning() {
			return missingBirthDateAndEmployeeAddressWarning;
		} 
        public void clearMissingEmployeeAddressWarning () {
        	missingEmployeeAddressWarning = false;
        }
        public boolean hasMissingEmployeeAddressWarning () { 
        	return missingEmployeeAddressWarning;
        }
        public void clearAllWarnings() {
            dateWarning = false;
            indicatorWarning = false;
            missingBirthDateWarning = false;
            missingEmployeeAddressWarning =false;
            missingBirthDateAndEmployeeAddressWarning = false;
        }

   
    }
    
    /**
     * Default ctor
     *
     */
    public EmployeeSummaryDetails() {
        optOutHistory = new EmployeeChangeHistoryVO();
        optOutHistory.setCurrentUser(optOutHistory.new ChangeUserInfo());
        enrollmentStatus = "";
        warnings = new EligibilityWarnings(); 
        eligibilityIndicatorStatus = 0;
        eligibilityDateStatus = 0;
        optOutStatus = 0;
        deferralStatus = 0;
    }

    /**
     * Constructor
     * 
     * @param firstName
     * @param lastName
     * @param ssn
     * @param employeeID
     * @param division
     * @param method
     * @param enrollmentStatus
     * @param processingDate
     * @param eligibleToEnroll
     * @param eligibilityDate
     * @param optOut
     * @param calculatedDeferralPct
     * @param beforeTaxDeferralPct
     * @param beforeTaxDeferralAmt
     * @param afterTaxDeferralPct
     * @param afterTaxDeferralAmt
     * @param atEnrollmentBeforeTaxDeferralPct
     * @param atEnrollmentBeforeTaxDeferralAmt
     * @param atEnrollmentAfterTaxDeferralPct
     * @param atEnrollmentAfterTaxDeferralAmt
     * @param profileId
     * @param participantInd
     * @param languageInd
     */
    public EmployeeSummaryDetails(String firstName, String middleInitial, String lastName, String ssn, 
            String employeeID, String division, String method, String enrollmentStatus,
            Date processingDate, String eligibleToEnroll, Date eligibilityDate, 
            boolean optOut, String calculatedDeferralPct, 
            String beforeTaxDeferralPct, String beforeTaxDeferralAmt,
            String afterTaxDeferralPct, String afterTaxDeferralAmt, 
            String atEnrollmentBeforeTaxDeferralPct, String atEnrollmentBeforeTaxDeferralAmt, 
            String atEnrollmentAfterTaxDeferralPct, String atEnrollmentAfterTaxDeferralAmt,
            String profileId, boolean participantInd, String mailingDate, String languageInd) {
        this();
        this.firstName = firstName;
        this.middleInitial = middleInitial != null ? middleInitial.toUpperCase():null;
        this.lastName = lastName;
        this.ssn = ssn;
        this.employerDesignatedID = employeeID;        
        this.division = division;
        this.enrollmentMethod = method;
        this.enrollmentStatus = enrollmentStatus;
        this.enrollmentProcessedDate = processingDate;
        this.eligibleToEnroll = eligibleToEnroll == null?null:eligibleToEnroll.trim();
        this.setEligibilityDate(eligibilityDate);
        this.optOut = optOut;
        this.contributionPct = calculatedDeferralPct;
        this.beforeTaxDeferralPct = beforeTaxDeferralPct;
        this.beforeTaxDeferralAmt = beforeTaxDeferralAmt;
        this.afterTaxDeferralPct = afterTaxDeferralPct;
        this.afterTaxDeferralAmt = afterTaxDeferralAmt;
        this.atEnrollmentBeforeTaxDeferralPct = atEnrollmentBeforeTaxDeferralPct;
        this.atEnrollmentBeforeTaxDeferralAmt = atEnrollmentBeforeTaxDeferralAmt;
        this.atEnrollmentAfterTaxDeferralPct = atEnrollmentAfterTaxDeferralPct;
        this.atEnrollmentAfterTaxDeferralAmt = atEnrollmentAfterTaxDeferralAmt;
        this.profileId = profileId;
        this.participantInd = participantInd;        
        this.mailingDate = mailingDate;
        this.languageInd = languageInd;
    }

    public EmployeeSummaryDetails(String profileId,String firstName, String middleInitial, String lastName, String ssn, 
            String employeeID, String division, String method, String enrollmentStatus,
            Date processingDate, String eligibleToEnroll,String optOutInd){
    	this.profileId = profileId;
    	this.firstName = firstName;
        this.middleInitial = middleInitial != null ? middleInitial.toUpperCase():null;
        this.lastName = lastName;
        this.ssn = ssn;
        this.employerDesignatedID = employeeID;        
        this.division = division;
        this.enrollmentMethod = method;
        this.enrollmentStatus = enrollmentStatus;
        this.enrollmentProcessedDate = processingDate;
        this.eligibleToEnroll = eligibleToEnroll == null?null:eligibleToEnroll.trim();
        this.autoEnrollOptOutInd = optOutInd;
    }
    /**
     * 
     * @return
     */
    public String getContributionPct() {
        return this.contributionPct;
    }

    /**
     * 
     * @param contributionPct
     */
    public void setContributionPct(String contributionPct) {
        this.contributionPct =  contributionPct;
    }

    /**
     * 
     * @return
     */
    public String getEligibilityDate() {
        if(this.eligibilityDate == null || "".equals(this.eligibilityDate)) {
            return "mm/dd/yyyy";
        }

        return this.eligibilityDate;
    }

    public Date getEligibilityDateAsDate() throws ParseException {
        Date eDate;
        
        if(this.eligibilityDate == null || "mm/dd/yyyy".equals(this.eligibilityDate)
                || "".equals(this.eligibilityDate)) {
            eDate = null;
        } else {
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); 
            dateFormatter.setLenient(false);
            
            eDate = dateFormatter.parse(eligibilityDate, pos);
            // If it cannot be parsed at all index is 0
            // If not everything is parseable than the index stops where the pasing stopped
            if(pos.getIndex() == 0 || pos.getIndex() < eligibilityDate.length()) {
                throw new ParseException("Cannot parse full date string", pos.getIndex());
            } 
        } 
        
        return eDate;
    }
    
    /**
     * 
     * @param eligibilityDate
     */
    public void setEligibilityDate(String eligibilityDate) {
        this.eligibilityDate = eligibilityDate;
    }
    
    public void setEligibilityDate(Date eligibilityDate) {    
        if(eligibilityDate == null) {
            this.eligibilityDate = "";
        } else {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); 
            dateFormatter.setLenient(false);
            
            this.eligibilityDate = dateFormatter.format(eligibilityDate);
        }
    }

    /**
     * Uses just the fields that can be updated from CensusSummaryDetails
     * 
     * @Override 
     */    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        final EmployeeSummaryDetails other = (EmployeeSummaryDetails) obj;
        
        if (this.getProfileId() == null) {
            if (other.getProfileId() != null)
                return false;
        } else if (!this.getProfileId().equals(other.getProfileId()))
            return false;
        
        if (this.getContributionPct() == null) {
            if (other.getContributionPct() != null)
                return false;
        } else if ("".equals(this.getContributionPct())) { 
            if(other.getContributionPct() != null) {
                if(!"".equals(other.getContributionPct())) {
                    return false;
                }
            } 
        } else if(!this.getContributionPct().equals(other.getContributionPct())) {
            return false;
        }            

        try {
            if (this.getEligibilityDateAsDate() == null) {
                if (other.getEligibilityDateAsDate() != null)
                    return false;
            } else if (!this.getEligibilityDateAsDate().equals(other.getEligibilityDateAsDate()))
                return false;
        } catch (ParseException e) {
            return false;
        }
        
        if (this.getEligibleToEnroll() == null) {
            if (other.getEligibleToEnroll() != null)
                return false;
        } else  if ("".equals(this.getEligibleToEnroll())) { 
            if(other.getEligibleToEnroll() != null) {
                if( !"".equals(other.getEligibleToEnroll())) {
                    return false;
                }
            } 
        } else if (!this.getEligibleToEnroll().equals(other.getEligibleToEnroll()))
            return false;

        if (this.getAutoEnrollOptOutInd() == null) {
            if (other.getAutoEnrollOptOutInd() != null)
                return false;
        } else  if ("".equals(this.getAutoEnrollOptOutInd())) { 
            if(other.getAutoEnrollOptOutInd() != null) {
                if( !"".equals(other.getAutoEnrollOptOutInd())) {
                    return false;
                }
            }
        } else if (!this.getAutoEnrollOptOutInd().equals(other.getAutoEnrollOptOutInd())){
        	return false;
        } else if (!this.getLanguageInd().equals(other.getLanguageInd()))
            return false;
        if (this.getLanguageInd() == null) {
            if (other.getLanguageInd() != null)
                return false;
        } else  if ("".equals(this.getLanguageInd())) { 
            if(other.getLanguageInd() != null) {
                if( !"".equals(other.getLanguageInd())) {
                    return false;
                }
            } 
        } else if (!this.getLanguageInd().equals(other.getLanguageInd()))
            return false;
       
        return true;
    }

    /**
     * Uses just the fields that can be updated from CensusSummaryDetails
     * 
     * @Override 
     */ 
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;

        result = PRIME * result + ((getContributionPct() == null) ? 0 : getContributionPct().hashCode());
        result = PRIME * result + ((getEligibilityDate() == null) ? 0 : getEligibilityDate().hashCode());
        result = PRIME * result + ((getEligibleToEnroll() == null) ? 0 : getEligibleToEnroll().hashCode());
        result = PRIME * result + ((getAutoEnrollOptOutInd() == null) ? 0 : getAutoEnrollOptOutInd().hashCode());
        result = PRIME * result + ((getProfileId() == null) ? 0 : getProfileId().hashCode());
        
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EmployeeSummaryDetails details = new EmployeeSummaryDetails();
        details.setEligibilityDate(this.eligibilityDate);
        details.setProfileId(this.profileId);
        details.setContributionPct(this.contributionPct);
        details.setEligibleToEnroll(this.eligibleToEnroll);
        details.setAutoEnrollOptOutInd(this.autoEnrollOptOutInd);
        details.setLanguageInd(this.languageInd);
        
        return details;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getSsn() {
        return ssn;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    

    public Date getEmploymentStatusEffDate() {
		return employmentStatusEffDate;
	}

	public void setEmploymentStatusEffDate(Date employmentStatusEffDate) {
		this.employmentStatusEffDate = employmentStatusEffDate;
	}
    
    public boolean isEmploymentStatusActiveOrBlank() {
        if(employmentStatus == null || 
                "A".equalsIgnoreCase(employmentStatus) ||
                "".equals(employmentStatus.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public Date getHireDate() {
        return hireDate;
    }

    public boolean isParticipantInd() {
    	return participantInd;
    }

    public String getDivision() {
        return division;
    }

    public String getEligibleToEnroll() {
        return this.eligibleToEnroll == null?"":this.eligibleToEnroll;
    }

    public void setEligibleToEnroll(String eligibleToEnrollInd) {
        this.eligibleToEnroll = eligibleToEnrollInd;
    }
    
    /**
     * Utility method based on eligibleToEnroll property
     * 
     * @return
     */
    public boolean isEligible() {
        if(this.eligibleToEnroll != null && "Y".equalsIgnoreCase(this.eligibleToEnroll)) {
            return true;
        }
        
        return false;
    }

    public String getEmployerDesignatedID() {
        return employerDesignatedID;
    }

    public void setEmployerDesignatedID(String employerDesignatedID) {
        this.employerDesignatedID = employerDesignatedID;
    }

    public String getEnrollmentMethod() {
        return enrollmentMethod;
    }

    public void setEnrollmentMethod(String enrollmentMethod) {
        this.enrollmentMethod = enrollmentMethod;
    }

    public Date getEnrollmentProcessedDate() {
        return enrollmentProcessedDate;
    }

    public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {
        this.enrollmentProcessedDate = enrollmentProcessedDate;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public boolean isOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public EmployeeChangeHistoryVO getOptOutHistory() {
        return optOutHistory;
    }

    public void setOptOutHistory(EmployeeChangeHistoryVO employeeChangeHistoryList) {
        this.optOutHistory = employeeChangeHistoryList;
    }

    public EligibilityWarnings getWarnings() {
        return warnings;
    }

    public int getDeferralStatus() {
        return deferralStatus;
    }

    public void setDeferralStatus(int deferralStatus) {
        this.deferralStatus = deferralStatus;
    }

    public int getEligibilityDateStatus() {
        return eligibilityDateStatus;
    }

    public void setEligibilityDateStatus(int eligibilityDateStatus) {
        this.eligibilityDateStatus = eligibilityDateStatus;
    }

    public int getEligibilityIndicatorStatus() {
        return eligibilityIndicatorStatus;
    }

    public void setEligibilityIndicatorStatus(int eligibilityIndicatorStatus) {
        this.eligibilityIndicatorStatus = eligibilityIndicatorStatus;
    }

    public int getOptOutStatus() {
        return optOutStatus;
    }

    public void setOptOutStatus(int optOutStatus) {
        this.optOutStatus = optOutStatus;
    }

    public String getAfterTaxDeferralAmt() {
        return afterTaxDeferralAmt;
    }

    public void setAfterTaxDeferralAmt(String afterTaxDeferralAmt) {
        this.afterTaxDeferralAmt = afterTaxDeferralAmt;
    }

    public String getAfterTaxDeferralPct() {
        return afterTaxDeferralPct;
    }

    public void setAfterTaxDeferralPct(String afterTaxDeferralPct) {
        this.afterTaxDeferralPct = afterTaxDeferralPct;
    }

    public String getBeforeTaxDeferralAmt() {
        return beforeTaxDeferralAmt;
    }

    public void setBeforeTaxDeferralAmt(String beforeTaxDeferralAmt) {
        this.beforeTaxDeferralAmt = beforeTaxDeferralAmt;
    }

    public String getBeforeTaxDeferralPct() {
        return beforeTaxDeferralPct;
    }

    public void setBeforeTaxDeferralPct(String beforeTaxDeferralPct) {
        this.beforeTaxDeferralPct = beforeTaxDeferralPct;
    }

    public Date getApplicablePlanEntryDate() {
        return applicablePlanEntryDate;
    }

    public void setApplicablePlanEntryDate(Date applicablePlanEntryDate) {
        this.applicablePlanEntryDate = applicablePlanEntryDate;
    }

    public List<EmployeeChangeHistoryVO> getStatusChanges() {
        return statusChanges;
    }

    public void setStatusChanges(List<EmployeeChangeHistoryVO> statusChanges) {
        this.statusChanges = statusChanges;
    }

    public String getAtEnrollmentAfterTaxDeferralAmt() {
        return atEnrollmentAfterTaxDeferralAmt;
    }

    public void setAtEnrollmentAfterTaxDeferralAmt(String atEnrollmentAfterTaxDeferralAmt) {
        this.atEnrollmentAfterTaxDeferralAmt = atEnrollmentAfterTaxDeferralAmt;
    }

    public String getAtEnrollmentAfterTaxDeferralPct() {
        return atEnrollmentAfterTaxDeferralPct;
    }

    public void setAtEnrollmentAfterTaxDeferralPct(String atEnrollmentAfterTaxDeferralPct) {
        this.atEnrollmentAfterTaxDeferralPct = atEnrollmentAfterTaxDeferralPct;
    }

    public String getAtEnrollmentBeforeTaxDeferralAmt() {
        return atEnrollmentBeforeTaxDeferralAmt;
    }

    public void setAtEnrollmentBeforeTaxDeferralAmt(String atEnrollmentBeforeTaxDeferralAmt) {
        this.atEnrollmentBeforeTaxDeferralAmt = atEnrollmentBeforeTaxDeferralAmt;
    }

    public String getAtEnrollmentBeforeTaxDeferralPct() {
        return atEnrollmentBeforeTaxDeferralPct;
    }

    public void setAtEnrollmentBeforeTaxDeferralPct(String atEnrollmentBeforeTaxDeferralPct) {
        this.atEnrollmentBeforeTaxDeferralPct = atEnrollmentBeforeTaxDeferralPct;
    }

    public String getAutoEnrollOptOutInd() {
        return autoEnrollOptOutInd;
    }

    public void setAutoEnrollOptOutInd(String autoEnrollOptOutInd) {
        this.autoEnrollOptOutInd = autoEnrollOptOutInd;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    
    /**
     * Tests if any error exist
     * 
     * @return
     */
    public boolean hasErrors() {
        if(eligibilityIndicatorStatus == 2 || 
           eligibilityDateStatus == 2 ||
           optOutStatus == 2 ||
           deferralStatus == 2) {
            return true;
        } 
        
        return false;
    }
    
    /**
     * Tests if any warnings exists
     * 
     * @return
     */
    public boolean hasWarnings() {
        if(eligibilityIndicatorStatus == 1 || 
            eligibilityDateStatus == 1 ||
            optOutStatus == 1 ||
            deferralStatus == 1 ||
            employeeAddressStatus ==1 ||
            birthDateStatus ==1)
            {
             return true;
         } 
         
         return false;
    }
    
    public boolean hasAlerts() {
        if(EmployeeEnrollmentSummaryReportData.WITHDRAWAL_ELECTION_90DAYS_INITIATED.equals(withdrawalElection90Days)||
           EmployeeEnrollmentSummaryReportData.WITHDRAWAL_ELECTION_90DAYS_FORMS_SUBMITTED.equals(withdrawalElection90Days)) {
             return true;
        }
        else
        {
           return false;
        }
    }
    
    public static class EmployeeComparator implements Comparator<EmployeeSummaryDetails>, Serializable {
        private static final long serialVersionUID = 3848281624303984832L;

        /**
         * Compare just the names
         */
        public int compare(EmployeeSummaryDetails esd1, EmployeeSummaryDetails esd2) {
            String lastName1 = "Z";
            String firstName1 = "Z";
            String middleInitial1= "A";
            String lastName2 = "Z";
            String firstName2 = "Z";
            String middleInitial2 = "A";
            
            int result = 0;
            
            if(esd1.getLastName() != null && esd1.getLastName().length() > 0) {
                lastName1 = esd1.getLastName(); 
            } 
              
            if(esd2.getLastName() != null && esd2.getLastName().length() > 0) {
                lastName2 = esd2.getLastName(); 
            } 
            
            if(esd1.getFirstName() != null && esd1.getFirstName().length() > 0) {
                firstName1 = esd1.getFirstName(); 
            } 
            
            if(esd2.getFirstName() != null && esd2.getFirstName().length() > 0) {
                firstName2 = esd2.getFirstName(); 
            } 
            
            if(esd1.getMiddleInitial() != null && esd1.getMiddleInitial().length() > 0) {
                middleInitial1 = esd1.getMiddleInitial(); 
            } 
            
            if(esd2.getMiddleInitial() != null && esd2.getMiddleInitial().length() > 0) {
                middleInitial2 = esd2.getMiddleInitial(); 
            } 
            
            result = lastName1.compareTo(lastName2);
            
            // Test if the last name is the same
            if(result == 0) {
                result = firstName1.compareTo(firstName2);
                // Test if the first name is the same 
                if(result == 0) {
                    // The result will the comparison of middle initals,
                    // because the last and first names are the same
                    result = middleInitial1.compareTo(middleInitial2);
                }
            } 
                
            return result;
        }
        
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getContributionStatus() {
		return contributionStatus;
	}

	public void setContributionStatus(String contributionStatus) {
		this.contributionStatus = contributionStatus;
	}

	public String getParticipantStatusCode() {
		return participantStatusCode;
	}

	public void setParticipantStatusCode(String participantStatusCode) {
		this.participantStatusCode = participantStatusCode;
	}

	public boolean isShowHistoryAndNameLink() {
		return showHistoryAndNameLink;
	}

	public void setShowHistoryAndNameLink(boolean showHistoryAndNameLink) {
		this.showHistoryAndNameLink = showHistoryAndNameLink;
	}
	
	public String getWithdrawalElection90Days() {
		return withdrawalElection90Days;
	}

	public void setWithdrawalElection90Days(String withdrawalElection90Days) {
		this.withdrawalElection90Days = withdrawalElection90Days;
	}

	public boolean isOptOutEditable() {
		return optOutEditable;
	}

	public void setOptOutEditable(boolean optOutEditable) {
		this.optOutEditable = optOutEditable;
	}
	
	public boolean isOptedOutNotVested() {
		return optedOutNotVested;
	}

	public void setOptedOutNotVested(boolean optedOutNotVested) {
		this.optedOutNotVested = optedOutNotVested;
	}
	
	public boolean isHasVestedMoneyOnly() {
		return hasVestedMoneyOnly;
	}

	public void setHasVestedMoneyOnly(boolean hasVestedMoneyOnly) {
		this.hasVestedMoneyOnly = hasVestedMoneyOnly;
	}

	/**
     * Utility method that returns the name as expected 
     * to be displayed on the screen
     * 
     * @return
     */
    public String getFullName() {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(processString(getLastName()));
        buffer.append(", ");
        buffer.append(processString(getFirstName()));
        if(getMiddleInitial() != null &&
            !"".equals(getMiddleInitial().trim())) {
            buffer.append(" ");
            buffer.append(processString(getMiddleInitial()));
        }     
        
        return buffer.toString();
    }
    
    
    /**
     * Utility method that prepares strings to be displayed 
     * 
     * @param field
     * @return
     */
    public static String processString(String field) {
        if(field == null) {
            return "";
        } else {
            return field.trim();
        }
    }
    
	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getMailingDate() {
		return mailingDate;
	}

	public void setLanguageInd(String languageInd) {
		this.languageInd = languageInd;
	}
    
	public String getLanguageInd() {
	     return this.languageInd == null?"":this.languageInd;
	}

	public void setEmployeeAddressStatus(int employeeAddressStatus) {
		this.employeeAddressStatus = employeeAddressStatus;
	}

	public int getEmployeeAddressStatus() {
		return employeeAddressStatus;
	}

	public void setBirthDateStatus(int birthDateStatus) {
		this.birthDateStatus = birthDateStatus;
	}

	public int getMissingBirthDateStatus() {
		return birthDateStatus;
	}
	// Added For EC project. -- START --
	public List<String> getPlanEntryDates() {
		return planEntryDates;
	}

	public void setPlanEntryDates(List<String> planEntryDates) {
		this.planEntryDates = planEntryDates;
	}
	//Added For EC project. -- END --

	public List<String> getEligibilityData() {
		return eligibilityData;
	}

	public void setEligibilityData(List<String> eligibilityData) {
		this.eligibilityData = eligibilityData;
	}

	public String getCompPeriodDateString() {
		return compPeriodDateString;
	}

	public void setCompPeriodDateString(String compPeriodDateString) {
		this.compPeriodDateString = compPeriodDateString;
	}

	public String getCompPeriodChangeDate() {
		return compPeriodChangeDate;
	}

	public void setCompPeriodChangeDate(String compPeriodChangeDate) {
		this.compPeriodChangeDate = compPeriodChangeDate;
	}

}
              
