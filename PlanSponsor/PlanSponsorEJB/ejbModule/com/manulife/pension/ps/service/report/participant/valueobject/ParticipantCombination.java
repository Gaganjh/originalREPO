package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.service.contract.valueobject.ParticipantWithLoansVO;
import com.manulife.pension.service.employee.util.EmployeeValidationError;

/**
 * This class is used for setting and getting the Report Data in CSV file Download.
 * @author Vishnu
 *
 */
public class ParticipantCombination implements Serializable {
	
	private String identifier;
	private String name;
	private SortedMap moneyTypeAmounts;
	private SortedMap loanAmounts;
	protected String firstName;
    protected String lastName;
	protected String ssn;
    protected String employeeNumber;
    protected Date birthDate;
    protected Date hireDate;
	protected String status;
    protected Date employeeStatusDate;
    protected String profileId;
    protected int contractId;
    protected boolean participantInd;
    private String namePrefix;
    protected String middleInitial;
    private String stateOfResidence;
    private String employeeProvidedEmail;
    protected String division;
    private Date enrollmentProcessedDate;
    private String enrollmentMethod;//Internet, Auto, Was Auto Enroll, Default, Paper
    private String enrollmentStatus;
    private String employerDesignatedID;
    private String eligibleToDeferInd;
    private Date eligibilityDate;
    private String optOut;
    
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String city = "";
    private String stateCode = "";
    private String zipCode = "";
    private String country = "";
    
    private Date planDate;
    private Integer planYTDHoursWorked;
    private Date planYTDHoursWorkedEffDate;
    private BigDecimal planYTDCompensation;
    private BigDecimal annualBaseSalary;
    private BigDecimal beforeTaxDeferralPercentage;
    private BigDecimal desigRothDeferralPercentage;
    private BigDecimal beforeTaxDeferralAmount;
    private BigDecimal desigRothDeferralAmount;
    private Integer previousYearsOfService;
    private Date previousYearsOfServiceEffDate;
    private String fullyVestedInd;
    private Date fullyVestedIndEffDate;
    private String maskSensitiveInfo;
    private String providedEligibilityDateInd;
    
    // list of warnings/errors found
    private Set censusErrors;
    
    // List of employee errors - should refactor to merge this later.
    private Set<EmployeeValidationError> employeeErrors;
    
    // object that holds all warning flags
    private CensusWarnings warnings;
    
	/**
	 * Default Constructor
	 */
	public ParticipantCombination() {
	}
	
	/**
	 * Parameterized Constructor.
	 * @param identifier
	 * @param firstName
	 * @param lastName
	 * @param moneyTypeAmounts
	 * @param loanAmounts
	 */
	
	public ParticipantCombination(ParticipantWithLoansVO participantData, 
				SortedMap moneyTypeAmounts, SortedMap loanAmounts) {

        warnings = new CensusWarnings(); 
        employeeErrors = new HashSet<EmployeeValidationError>();
        
        this.ssn = participantData.getSsn();
        this.identifier = participantData.getSsn();
        this.employeeNumber = participantData.getEmployeeNumber();
		this.firstName = participantData.getFirstName();
		this.lastName = participantData.getLastName();
		this.name = lastName + ", " + firstName;
		this.middleInitial = participantData.getMiddleInitial();
        this.namePrefix = participantData.getNamePrefix();
        this.addressLine1 = participantData.getAddressLine1();
        this.addressLine2 = participantData.getAddressLine2();
        this.city = participantData.getCity();
        this.stateCode = participantData.getStateCode();
        this.zipCode = participantData.getZipCode();
        this.country = participantData.getCountry();
        this.stateOfResidence = participantData.getStateOfResidence();
        this.employeeProvidedEmail = participantData.getEmployeeProvidedEmail();
        this.division = participantData.getDivision();
		this.birthDate = participantData.getBirthDate();
        this.hireDate = participantData.getHireDate();
		this.status = participantData.getStatus();
        this.employeeStatusDate = participantData.getEmployeeStatusDate();
        this.eligibleToDeferInd = participantData.getEligibleToDeferInd();
        this.eligibilityDate = participantData.getEligibilityDate();
        this.optOut = participantData.getOptOut();
        
        // Trim the planYTDHoursWorked value, and check against null (and a valid number)
        planYTDHoursWorked = participantData.getPlanYTDHoursWorked();
  
        this.planYTDCompensation = participantData.getPlanYTDCompensation();
        this.planYTDHoursWorkedEffDate = participantData.getPlanYTDHoursWorkedEffDate();

        // Trim the previousYearsOfService value, and check against null (and a valid number)
        previousYearsOfService = participantData.getPreviousYearsOfService();
 
        this.previousYearsOfServiceEffDate = participantData.getPreviousYearsOfServiceEffDate();
        this.annualBaseSalary = participantData.getAnnualBaseSalary();
        this.beforeTaxDeferralPercentage = participantData.getBeforeTaxDeferralPercentage();
        this.desigRothDeferralPercentage = participantData.getDesigRothDeferralPercentage();
        this.beforeTaxDeferralAmount = participantData.getBeforeTaxDeferralAmount();
        this.desigRothDeferralAmount = participantData.getDesigRothDeferralAmount();
		this.profileId = participantData.getProfileId();
        this.contractId = participantData.getContractId();
        //this.participantInd = participantData.getParticipantInd();
        this.fullyVestedInd = participantData.getFullyVestedInd();
        this.fullyVestedIndEffDate = participantData.getFullyVestedIndEffDate();
        this.maskSensitiveInfo = participantData.getMaskSensitiveInfo();
        this.providedEligibilityDateInd = participantData.getProvidedEligibilityDateInd();
        this.employeeErrors = new HashSet<EmployeeValidationError>();
	
		
		this.moneyTypeAmounts = moneyTypeAmounts;
		this.loanAmounts = loanAmounts;
	}
	
	/**
	 * Parameterized Constructor.
	 * @param identifier
	 * @param name
	 * @param moneyTypeAmounts
	 * @param loanAmounts
	 */
	public ParticipantCombination(String identifier, String name, 
			SortedMap moneyTypeAmounts, SortedMap loanAmounts) {
		this.identifier = identifier;
		this.name = name;
		this.moneyTypeAmounts = moneyTypeAmounts;
		this.loanAmounts = loanAmounts;
	}
	
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return Returns the loanAmounts.
	 */
	public SortedMap getLoanAmounts() {
		return loanAmounts;
	}

	/**
	 * @return Returns the moneyTypeAmounts.
	 */
	public SortedMap getMoneyTypeAmounts() {
		return moneyTypeAmounts;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param loanAmounts The loanAmounts to set.
	 */
	public void setLoanAmounts(SortedMap loanAmounts) {
		this.loanAmounts = loanAmounts;
	}
	
	/**
	 * @param moneyTypeAmounts The moneyTypeAmounts to set.
	 */
	public void setMoneyTypeAmounts(SortedMap moneyTypeAmounts) {
		this.moneyTypeAmounts = moneyTypeAmounts;
	}
	    
    /**
     * Default constructor
     *
     */
    public void setCensusSummaryDetails() {
        warnings = new CensusWarnings(); 
        employeeErrors = new HashSet<EmployeeValidationError>();
    }

	/**
	 * 
	 * @return Birth Date.
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * 
	 * @return First Name
	 */
	public String getFirstName() {
		return firstName;
	}
	
    /**
     * 
     * @return Last Name
     */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * 
	 * @return profileId
	 */
	public String getProfileId() {
		return profileId;
	}
	
	/**
	 * 
	 * @return ssn
	 */
	public String getSsn() {
		return ssn;
	}
	
	/**
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @return Hire Date
	 */
	public Date getHireDate() {
		return hireDate;
	}
	
	/**
	 * 
	 * @return Participant Indicater
	 */
    public boolean isParticipantInd() {
        return participantInd;
    }
    
    /**
     * 
     * @return Division
     */
    public String getDivision() {
        return division;
    }
    
    /**
     * 
     * @return Eligibility Date
     */
    public Date getEligibilityDate() {
        return eligibilityDate;
    }
    
    /**
     * 
     * @param Eligibility Date
     */
    public void setEligibilityDate(Date eligibilityDate) {
        this.eligibilityDate = eligibilityDate;
    }
    
    /**
     * 
     * @return Eligible To Defer Indicator
     */
    public String getEligibleToDeferInd() {
        return eligibleToDeferInd;
    }
    
    /**
     * 
     * @param eligibleToDeferInd
     */
    public void setEligibleToDeferInd(String eligibleToDeferInd) {
        this.eligibleToDeferInd = eligibleToDeferInd;
    }
    
    /**
     * 
     * @return Employer Designated ID
     */
    public String getEmployerDesignatedID() {
        return employerDesignatedID;
    }
    
    /**
     * 
     * @param employerDesignatedID
     */
    public void setEmployerDesignatedID(String employerDesignatedID) {
        this.employerDesignatedID = employerDesignatedID;
    }
    
    /**
     * 
     * @return Enrollment Method
     */
    public String getEnrollmentMethod() {
        return enrollmentMethod;
    }
    
    /**
     * 
     * @param enrollmentMethod
     */
    public void setEnrollmentMethod(String enrollmentMethod) {
        this.enrollmentMethod = enrollmentMethod;
    }
    
    /**
     * 
     * @return Enrollment Processed Date
     */
    public Date getEnrollmentProcessedDate() {
        return enrollmentProcessedDate;
    }
    
    /**
     * 
     * @param enrollmentProcessedDate
     */
    public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {
        this.enrollmentProcessedDate = enrollmentProcessedDate;
    }
    
    /**
     * 
     * @return Enrollment Status
     */
    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }
    
    /**
     * 
     * @param enrollmentStatus
     */
    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
    
    /**
     * 
     * @return OptOut
     */
    public String getOptOut() {
        return optOut;
    }
    
    /**
     * 
     * @param optOut
     */
    public void setOptOut(String optOut) {
        this.optOut = optOut;
    }
    
    /**
     * 
     * @param profileId
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    
    /**
     * 
     * @return Employee Number
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    /**
     * 
     * @return Annual Base Salary
     */
    public BigDecimal getAnnualBaseSalary() {
        return annualBaseSalary;
    }
    
    /**
     * 
     * @return Employee Provided Email
     */
    public String getEmployeeProvidedEmail() {
        return employeeProvidedEmail;
    }
    
    /**
     * 
     * @return Employee Status Date
     */
    public Date getEmployeeStatusDate() {
        return employeeStatusDate;
    }
    
    /**
     * 
     * @return Middle Initial
     */
    public String getMiddleInitial() {
        return middleInitial;
    }
    
    /**
     * 
     * @return Name Prefix
     */
    public String getNamePrefix() {
        return namePrefix;
    }
    
    /**
     * 
     * @return PlanDate
     */
    public Date getPlanDate() {
        return planDate;
    }
    
    /**
     * 
     * @return Plan YTD Compensation
     */
    public BigDecimal getPlanYTDCompensation() {
        return planYTDCompensation;
    }
    
    /**
     * 
     * @return plan YTD Hours Worked
     */
    public Integer getPlanYTDHoursWorked() {
        return planYTDHoursWorked;
    }
    
    /**
     * 
     * @return plan YTD Hours Worked Effective Date
     */
    public Date getPlanYTDHoursWorkedEffDate() {
        return planYTDHoursWorkedEffDate;
    }
    
    /**
     * 
     * @return Previous Years Of Service
     */
    public Integer getPreviousYearsOfService() {
        return previousYearsOfService;
    }
    
    /**
     * 
     * @return State Of Residence
     */
    public String getStateOfResidence() {
        return stateOfResidence;
    }
    
    /**
     * 
     * @return Address Line1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * 
     * @return Address Line2
     */
    public String getAddressLine2() {
        if (StringUtils.isEmpty(addressLine2)) {
            return "";
        } else {
    	    return addressLine2;
	    }
    }

    /**
     * 
     * @return City
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     * @return Country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @return State Code
     */
    public String getStateCode() {
        return stateCode;
    }
    
    /**
     * 
     * @return ZipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 
     * @return Formatted ZipCode
     */
    public String getFormattedZipCode() {
        String rawZip = zipCode;
        if (rawZip==null) return "";
        rawZip=rawZip.trim(); 
        if ("USA".equalsIgnoreCase(this.getCountry().trim())) {
            if (rawZip.length()>5) {
                return rawZip.substring(0, 5)+"-"+rawZip.substring(5);
            } else {
                return rawZip;
            }
        } else {
            return rawZip;
        }
    }
    
    
    /**
     * Gets the wholeName. "Last Name, First Name Middle Initial"
     * 
     * @return Returns a String
     */
    public String getWholeName() {
            StringBuffer buff = new StringBuffer(lastName);
            buff.append(", ");
            buff.append(firstName);
            
            if (StringUtils.isNotEmpty(middleInitial)) {
                buff.append(" ").append(middleInitial);
            }
            return buff.toString();
    }
    
    /**
     * This class is used for setting and getting Census Warnings.
     * @author Vishnu
     *
     */
    public class CensusWarnings implements Serializable {

        private static final long serialVersionUID = 2319416675594517288L; 
        private boolean warnings;
        private String warningDescription = "";
        
        /**
         * 
         * @return Warnings
         */
        public boolean hasWarnings() {
            return warnings;
        }
        
        /**
         * 
         * @param warnings
         */
        public void setWarnings(boolean warnings) {
            this.warnings = warnings;
        }

        /**
         * 
         * @param desc
         */
        public void setWarningDescription(String desc) {
            if (warningDescription.equals("")) {
                warningDescription = desc;
            } else {
                warningDescription = warningDescription + "\n" + desc;
            }
            
        }
        
        /**
         * 
         * @return Warning Description
         */
        public String getWarningDescription() {
            return warningDescription;
        }

    }

    /**
     * 
     * @return Warnings.
     */
    public CensusWarnings getWarnings() {
        return warnings;
    }

    /**
     * 
     * @param warnings
     */
    public void setWarnings(CensusWarnings warnings) {
        this.warnings = warnings;
    }

    /**
     * 
     * @return Contract Id
     */
    public int getContractId() {
        return contractId;
    }
    
    /**
     * Utility method based on eligibleToDeferInd property
     * 
     * @return
     */
    public boolean isEligible() {
        if(eligibleToDeferInd != null && "Y".equalsIgnoreCase(eligibleToDeferInd)) {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @return Before Tax Deferral Amount
     */
    public BigDecimal getBeforeTaxDeferralAmount() {
        return beforeTaxDeferralAmount;
    }

    /**
     * 
     * @param beforeTaxDeferralAmount
     */
    public void setBeforeTaxDeferralAmount(BigDecimal beforeTaxDeferralAmount) {
        this.beforeTaxDeferralAmount = beforeTaxDeferralAmount;
    }

    /**
     * 
     * @return Before Tax Deferral Percentage
     */
    public BigDecimal getBeforeTaxDeferralPercentage() {
        return beforeTaxDeferralPercentage;
    }

    /**
     * 
     * @param beforeTaxDeferralPercentage
     */
    public void setBeforeTaxDeferralPercentage(BigDecimal beforeTaxDeferralPercentage) {
        this.beforeTaxDeferralPercentage = beforeTaxDeferralPercentage;
    }

    /**
     * 
     * @return Designated Roth Deferral Amount
     */
    public BigDecimal getDesigRothDeferralAmount() {
        return desigRothDeferralAmount;
    }

    /**
     * 
     * @param desigRothDeferralAmount
     */
    public void setDesigRothDeferralAmount(BigDecimal desigRothDeferralAmount) {
        this.desigRothDeferralAmount = desigRothDeferralAmount;
    }

    /**
     * 
     * @return Designated Roth Deferral Percentage
     */
    public BigDecimal getDesigRothDeferralPercentage() {
        return desigRothDeferralPercentage;
    }

    /**
     * 
     * @param desigRothDeferralPercentage
     */
    public void setDesigRothDeferralPercentage(BigDecimal desigRothDeferralPercentage) {
        this.desigRothDeferralPercentage = desigRothDeferralPercentage;
    }

    /**
     * 
     * @return Census Errors
     */
    public Set getCensusErrors() {
        return censusErrors;
    }

    /**
     * 
     * @param censusErrors
     */
    public void setCensusErrors(Set censusErrors) {
        this.censusErrors = censusErrors;
    }

    /**
     * 
     * @return Fully Vested Indicator
     */
    public String getFullyVestedInd() {
        return fullyVestedInd;
    }

    /**
     * 
     * @return Fully Vested Indicator Effective Date
     */
    public Date getFullyVestedIndEffDate() {
        return fullyVestedIndEffDate;
    }

    /**
     * 
     * @return Previous Years Of  Service Effective Date
     */
    public Date getPreviousYearsOfServiceEffDate() {
        return previousYearsOfServiceEffDate;
    }

    /**
     * 
     * @return Mask Sensitive Info
     */
    public String getMaskSensitiveInfo() {
        return maskSensitiveInfo;
    }

    /**
     * @return Set<EmployeeValidationError> - The employeeErrors.
     */
    public Set<EmployeeValidationError> getEmployeeErrors() {
        return employeeErrors;
    }

    /**
     * @param employeeErrors - The employeeErrors to set.
     */
    public void setEmployeeErrors(Set<EmployeeValidationError> employeeErrors) {
        this.employeeErrors = employeeErrors;
    }

    /**
     * 
     * @return Provided Eligibility Date Indicator
     */
	public String getProvidedEligibilityDateInd() {
		return providedEligibilityDateInd;
	}

	/**
	 * This method is Setting Census Summary Details
	 * @param censusSummaryDetails
	 */
	public void setCensusSummaryDetails(
			ParticipantWithLoansVO censusSummaryDetails) {}
	
}
