package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.manulife.pension.service.employee.util.EmployeeValidationError;

public class CensusSummaryDetails implements Details, Serializable {

	private static final long serialVersionUID = 1L;

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
     * Default constructor
     *
     */
    public CensusSummaryDetails() {
        warnings = new CensusWarnings(); 
        employeeErrors = new HashSet<EmployeeValidationError>();
    }

	public CensusSummaryDetails(String ssn, String employeeNumber, String firstName, String lastName, 
            String middleInitial, String namePrefix, String addressLine1, String addressLine2,
            String city, String stateCode, String zipCode, String country, String stateOfResidence,
            String email, String division, Date birthDate, Date hireDate,  String status, 
            Date statusDate, String eligibleInd, Date eligibilityDate, String optOutInd,
            String planYTDHoursWorked, BigDecimal planYTDCompensation, Date planYTDHoursWorkedEffDate,
            String previousYearsOfService, Date previousYearsOfServiceEffDate, BigDecimal annualBaseSalary, 
            BigDecimal beforeTaxDeferralPercentage, BigDecimal desigRothDeferralPercentage, 
            BigDecimal beforeTaxDeferralAmount, BigDecimal desigRothDeferralAmount, String profileId, 
            int contractId, boolean participantInd, String fullyVestedInd, Date fullyVestedIndEffDate, 
            String maskSensitiveInfo,String providedEligibilityDateInd) {
        this();
        this.ssn = ssn;
        this.employeeNumber = employeeNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleInitial = middleInitial;
        this.namePrefix = namePrefix;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.stateCode = stateCode;
        this.zipCode = zipCode;
        this.country = country;
        this.stateOfResidence = stateOfResidence;
        this.employeeProvidedEmail = email;
        this.division = division;
		this.birthDate = birthDate;
        this.hireDate = hireDate;
		this.status = status;
        this.employeeStatusDate = statusDate;
        this.eligibleToDeferInd = eligibleInd;
        this.eligibilityDate = eligibilityDate;
        this.optOut = optOutInd;

        // Trim the planYTDHoursWorked value, and check against null (and a valid number)
        planYTDHoursWorked = StringUtils.trimToNull(planYTDHoursWorked);
        if (planYTDHoursWorked != null && NumberUtils.isNumber(planYTDHoursWorked)) {
            this.planYTDHoursWorked = new Integer(planYTDHoursWorked);
        }
        this.planYTDCompensation = planYTDCompensation;
        this.planYTDHoursWorkedEffDate = planYTDHoursWorkedEffDate;

        // Trim the previousYearsOfService value, and check against null (and a valid number)
        previousYearsOfService = StringUtils.trimToNull(previousYearsOfService);
        if (previousYearsOfService != null && NumberUtils.isNumber(previousYearsOfService)) {
            this.previousYearsOfService = new Integer(previousYearsOfService);
        }
        this.previousYearsOfServiceEffDate = previousYearsOfServiceEffDate;
        this.annualBaseSalary = annualBaseSalary;
        this.beforeTaxDeferralPercentage = beforeTaxDeferralPercentage;
        this.desigRothDeferralPercentage = desigRothDeferralPercentage;
        this.beforeTaxDeferralAmount = beforeTaxDeferralAmount;
        this.desigRothDeferralAmount = desigRothDeferralAmount;
		this.profileId = profileId;
        this.contractId = contractId;
        this.participantInd = participantInd;
        this.fullyVestedInd = fullyVestedInd;
        this.fullyVestedIndEffDate = fullyVestedIndEffDate;
        this.maskSensitiveInfo = maskSensitiveInfo;
        this.providedEligibilityDateInd = providedEligibilityDateInd;
        this.employeeErrors = new HashSet<EmployeeValidationError>();
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

	public String getStatus() {
		return status;
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

    public Date getEligibilityDate() {
        return eligibilityDate;
    }

    public void setEligibilityDate(Date eligibilityDate) {
        this.eligibilityDate = eligibilityDate;
    }

    public String getEligibleToDeferInd() {
        return eligibleToDeferInd;
    }

    public void setEligibleToDeferInd(String eligibleToDeferInd) {
        this.eligibleToDeferInd = eligibleToDeferInd;
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

    public String getOptOut() {
        return optOut;
    }

    public void setOptOut(String optOut) {
        this.optOut = optOut;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }


    public BigDecimal getAnnualBaseSalary() {
        return annualBaseSalary;
    }

    public String getEmployeeProvidedEmail() {
        return employeeProvidedEmail;
    }

    public Date getEmployeeStatusDate() {
        return employeeStatusDate;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public BigDecimal getPlanYTDCompensation() {
        return planYTDCompensation;
    }

    public Integer getPlanYTDHoursWorked() {
        return planYTDHoursWorked;
    }

    public Date getPlanYTDHoursWorkedEffDate() {
        return planYTDHoursWorkedEffDate;
    }

    public Integer getPreviousYearsOfService() {
        return previousYearsOfService;
    }

    public String getStateOfResidence() {
        return stateOfResidence;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        if (StringUtils.isEmpty(addressLine2)) {
            return "";
        } else {
    	    return addressLine2;
	    }
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getStateCode() {
        return stateCode;
    }
    
    public String getZipCode() {
        return zipCode;
    }

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
    
    public class CensusWarnings implements Serializable {

        private static final long serialVersionUID = 2319416675594517288L;
        
        private boolean warnings;
        private String warningDescription = "";
        
        public boolean hasWarnings() {
            return warnings;
        }
        
        public void setWarnings(boolean warnings) {
            this.warnings = warnings;
        }

        public void setWarningDescription(String desc) {
            if (warningDescription.equals("")) {
                warningDescription = desc;
            } else {
                warningDescription = warningDescription + "\n" + desc;
            }
            
        }
        
        public String getWarningDescription() {
            return warningDescription;
        }

    }

    public CensusWarnings getWarnings() {
        return warnings;
    }

    public void setWarnings(CensusWarnings warnings) {
        this.warnings = warnings;
    }

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

    public BigDecimal getBeforeTaxDeferralAmount() {
        return beforeTaxDeferralAmount;
    }

    public void setBeforeTaxDeferralAmount(BigDecimal beforeTaxDeferralAmount) {
        this.beforeTaxDeferralAmount = beforeTaxDeferralAmount;
    }

    public BigDecimal getBeforeTaxDeferralPercentage() {
        return beforeTaxDeferralPercentage;
    }

    public void setBeforeTaxDeferralPercentage(BigDecimal beforeTaxDeferralPercentage) {
        this.beforeTaxDeferralPercentage = beforeTaxDeferralPercentage;
    }

    public BigDecimal getDesigRothDeferralAmount() {
        return desigRothDeferralAmount;
    }

    public void setDesigRothDeferralAmount(BigDecimal desigRothDeferralAmount) {
        this.desigRothDeferralAmount = desigRothDeferralAmount;
    }

    public BigDecimal getDesigRothDeferralPercentage() {
        return desigRothDeferralPercentage;
    }

    public void setDesigRothDeferralPercentage(BigDecimal desigRothDeferralPercentage) {
        this.desigRothDeferralPercentage = desigRothDeferralPercentage;
    }

    public Set getCensusErrors() {
        return censusErrors;
    }

    public void setCensusErrors(Set censusErrors) {
        this.censusErrors = censusErrors;
    }

    public String getFullyVestedInd() {
        return fullyVestedInd;
    }

    public Date getFullyVestedIndEffDate() {
        return fullyVestedIndEffDate;
    }

    public Date getPreviousYearsOfServiceEffDate() {
        return previousYearsOfServiceEffDate;
    }

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

	public String getProvidedEligibilityDateInd() {
		return providedEligibilityDateInd;
	}

}
