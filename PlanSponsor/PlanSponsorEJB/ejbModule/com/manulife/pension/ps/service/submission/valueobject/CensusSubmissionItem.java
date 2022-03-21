package com.manulife.pension.ps.service.submission.valueobject;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.submission.SubmissionCaseStatus;

public class CensusSubmissionItem extends Address implements SubmissionCaseStatus {

	private static final long serialVersionUID = 1L;
    
    public static final int MAX_FIRST_NAME_LENGTH = 18;
    public static final int MAX_LAST_NAME_LENGTH = 20;
    public static final int MAX_DATE_LENGTH = 10;
    public static final int MAX_EMAIL_LENGTH = 30;
    public static final int MAX_ADDRESS_LENGTH = 30;
    public static final int MAX_PREFIX_LENGTH = 4;
    public static final int MAX_CITY_LENGTH = 25;
    public static final int MAX_STATE_LENGTH = 2;
    public static final int MAX_ZIP_LENGTH = 9;
    public static final int MAX_COUNTRY_LENGTH = 30;
    public static final int MAX_NUMERIC_LENGTH = 12;   // 11 + 1
    public static final int MAX_PERC_LENGTH = 7;       // 6 + 1
    public static final int MAX_EMP_ID_LENGTH = 9;
    public static final int MAX_SSN_LENGTH = 9;
    public static final int MAX_DIVISION_LENGTH = 25;
    public static final int MAX_DEFFERAL_LENGTH = 10;  // 9 + 1
    

	private Integer errorCategory;
	private Integer submissionId;
    private Long profileId;
	private String firstName;
    private String lastName;
	private Date processedTS;
	private String processStatus; 
	private String errorConditionString;
	private Integer sourceRecordNo;
    private Integer sequenceNumber;
	private String userId;
	private String createdUserId;
	private Date createdTS;
	private String lastUpdatedUserId;
    private String eligibilityDate;
    private String eligibilityClass;
    private String eligibilityIndicator;
    private String birthDate;
    private String hireDate;
    private String employeeNumber;
    private String stateOfResidence;
    private String employeeProvidedEmail;
    private String division;
    private String employeeStatus;
    private String employeeStatusDate;
    private String namePrefix ;
    private String middleInitial;
        
    private String planDate;
    private String planYTDHoursWorked;
    private String planYTDHoursWorkedEffDate;
    private String planYTDCompensation;
    private String annualBaseSalary;
    private String beforeTaxDeferralPerc;
    private String desigRothDeferralPerc;
    private String beforeTaxDeferralAmt;
    private String desigRothDeferralAmt;
    private String previousYearsOfService;
    private Date vestedYearsOfServiceEffDate;
    private String optOutIndicator;
	
	private String displayStatus;
	private int statusSortOder;
	private Collection errors;
    private int contractNumberStatus;
    private int participantIdStatus;
    private int firstNameStatus;
    private int lastNameStatus;
	private int address1Status;
    private int address2Status;
	private int cityStatus;
	private int stateStatus;
	private int zipStatus;
    private int countryStatus;
	private int ssnStatus;
    private int employeeNumberStatus;
    private int divisionStatus;
    private int birthDateStatus;
    private int hireDateStatus;
    private int stateOfResidenceStatus;
    private int namePrefixStatus;
    private int middleInitialStatus;
    private int employeeProvidedEmailStatus;
    private int employeeStatusStatus;
    private int employeeStatusDateStatus;
    private int planYTDHoursWorkedStatus;
    private int planYTDHoursWorkedEffDateStatus;
    private int eligibilityIndicatorStatus;
    private int eligibilityDateStatus;
    private int optOutIndicatorStatus;
    private int beforeTaxDeferralPercStatus;
    private int desigRothDeferralPercStatus;
    private int beforeTaxDeferralAmtStatus;
    private int desigRothDeferralAmtStatus;
    private int planYTDCompensationStatus;
    private int annualBaseSalaryStatus;
    private int previousYearsOfServiceStatus;
    private int vestedYearsOfServiceEffDateStatus;
    private boolean doubleRowHeight;
	
	public static final int OK = 0;
	public static final int NON_EDITABLE_WARNING = 1;
    public static final int EDITABLE_WARNING = 2;
	public static final int ERROR = 3;

	/**
	 * @return Returns the createdTS.
	 */
	public Date getCreatedTS() {
		return createdTS;
	}
	/**
	 * @param createdTS The createdTS to set.
	 */
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	/**
	 * @return Returns the createdUserId.
	 */
	public String getCreatedUserId() {
		return createdUserId;
	}
	/**
	 * @param createdUserId The createdUserId to set.
	 */
	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}
	/**
	 * @return Returns the errorConditionString.
	 */
	public String getErrorConditionString() {
		return errorConditionString;
	}
	/**
	 * @param errorConditionString The errorConditionString to set.
	 */
	public void setErrorConditionString(String errorConditionString) {
		this.errorConditionString = errorConditionString;
	}

	/**
	 * @return Returns the lastUpdatedUserId.
	 */
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	/**
	 * @param lastUpdatedUserId The lastUpdatedUserId to set.
	 */
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	/**
	 * @return Returns the processedTS.
	 */
	public Date getProcessedTS() {
		return processedTS;
	}
	/**
	 * @param processedTS The processedTS to set.
	 */
	public void setProcessedTS(Date processedTS) {
		this.processedTS = processedTS;
	}
	/**
	 * @return Returns the processStatus.
	 */
	public String getProcessStatus() {
		return processStatus;
	}
	/**
	 * @param processStatusCode The processStatus to set.
	 */
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	/**
	 * @return Returns the sourceRecordNo.
	 */
	public Integer getSourceRecordNo() {
		return sourceRecordNo;
	}
	/**
	 * @param sourceRecordNo The sourceRecordNo to set.
	 */
	public void setSourceRecordNo(Integer sourceRecordNo) {
		this.sourceRecordNo = sourceRecordNo;
	}
	/**
	 * @return Returns the submissionId.
	 */
	public Integer getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the displayStatus.
	 */
	public String getDisplayStatus() {
		return displayStatus;
	}
	/**
	 * @param displayStatus The displayStatus to set.
	 */
	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}
	/**
	 * @return Returns the statusSortOder.
	 */
	public int getStatusSortOder() {
		return statusSortOder;
	}
	/**
	 * @param statusSortOder The statusSortOder to set.
	 */
	public void setStatusSortOder(int statusSortOder) {
		this.statusSortOder = statusSortOder;
	}
	/**
	 * @return Returns the errors.
	 */
	public Collection getErrors() {
		return errors;
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(Collection errors) {
		this.errors = errors;
	}

	/**
	 * @return Returns the cityStatus.
	 */
	public int getCityStatus() {
		return cityStatus;
	}
	/**
	 * @param cityStatus The cityStatus to set.
	 */
	public void setCityStatus(int cityStatus) {
		this.cityStatus = cityStatus;
	}
    public String getCityTruncated() {
        return getTruncatedValue(city, MAX_CITY_LENGTH);
    }
	/**
	 * @return Returns the stateStatus.
	 */
	public int getStateStatus() {
		return stateStatus;
	}
	/**
	 * @param stateStatus The stateStatus to set.
	 */
	public void setStateStatus(int stateStatus) {
		this.stateStatus = stateStatus;
	}
    public String getStateCodeTruncated() {
        return getTruncatedValue(stateCode, MAX_STATE_LENGTH);
    }
	/**
	 * @return Returns the zipStatus.
	 */
	public int getZipStatus() {
		return zipStatus;
	}
	/**
	 * @param zipStatus The zipStatus to set.
	 */
	public void setZipStatus(int zipStatus) {
		this.zipStatus = zipStatus;
	}

	/**
	 * @return Returns the ssnStatus.
	 */
	public int getSsnStatus() {
		return ssnStatus;
	}
	/**
	 * @param ssnStatus The ssnStatus to set.
	 */
	public void setSsnStatus(int ssnStatus) {
		this.ssnStatus = ssnStatus;
	}
    public String getFirstName() {
        return firstName;
    }
    
    public String getFirstNameTruncated() {
        return getTruncatedValue(firstName, MAX_FIRST_NAME_LENGTH);
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLastNameTruncated() {
        return getTruncatedValue(lastName, MAX_LAST_NAME_LENGTH);
    }
    
    public String getEligibilityIndicator() {
        return eligibilityIndicator;
    }
    public void setEligibilityIndicator(String eligibilityIndicator) {
        this.eligibilityIndicator = eligibilityIndicator;
    }
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    /**
     * If contract sort option is "SS", populates the field with the 
     * ssn, otherwise populates the field with employee number
     * 
     * @param contractSortOptionCode 
     */
    public void setEmpId(String contractSortOptionCode) {
        this.empId = Constants.SSN_SORT_OPTION_CODE.equals(contractSortOptionCode) ?
                ssn : employeeNumber;
    }
    
    public String getEmployeeProvidedEmail() {
        return employeeProvidedEmail;
    }
    public String getEmployeeProvidedEmailTruncated() {
        return getTruncatedValue(employeeProvidedEmail, MAX_EMAIL_LENGTH);
    }
    
    public String getAddresLine1Truncated() {
        return getTruncatedValue(addressLine1, MAX_ADDRESS_LENGTH);
    }
    
    public String getAddresLine2Truncated() {
        return getTruncatedValue(addressLine2, MAX_ADDRESS_LENGTH);
    }
    
    public void setEmployeeProvidedEmail(String employeeProvidedEmail) {
        this.employeeProvidedEmail = employeeProvidedEmail;
    }
    public String getStateOfResidence() {
        return stateOfResidence;
    }
    public String getStateOfResidenceTruncated() {
        return getTruncatedValue(stateOfResidence, MAX_STATE_LENGTH);
    }
    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }
    
    public String getDivision() {
        return division;
    }
    public String getDivisionTruncated() {
        return getTruncatedValue(division, MAX_DIVISION_LENGTH);
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public String getEmployeeStatus() {
        return employeeStatus;
    }
    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
    public String getMiddleInitial() {
        return middleInitial;
    }
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    public String getNamePrefix() {
        return namePrefix;
    }
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    public String getNamePrefixTruncated() {
        return getTruncatedValue(namePrefix, MAX_PREFIX_LENGTH);
    }
    
    public String getPlanYTDHoursWorked() {
        return planYTDHoursWorked;
    }
    public void setPlanYTDHoursWorked(String planYTDHoursWorked) {
        this.planYTDHoursWorked = planYTDHoursWorked;
    }
    public String getPreviousYearsOfService() {
        return previousYearsOfService;
    }
    public void setPreviousYearsOfService(String previousYearsOfService) {
        this.previousYearsOfService = previousYearsOfService;
    }
    public Long getProfileId() {
        return profileId;
    }
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
    public Integer getErrorCategory() {
        return errorCategory;
    }
    public void setErrorCategory(Integer errorCategory) {
        this.errorCategory = errorCategory;
    }
    public String getOptOutIndicator() {
        return optOutIndicator;
    }
    public void setOptOutIndicator(String optOutIndicator) {
        this.optOutIndicator = optOutIndicator;
    }
    public String getEligibilityClass() {
        return eligibilityClass;
    }
    public void setEligibilityClass(String eligibilityClass) {
        this.eligibilityClass = eligibilityClass;
    }
    public int getBirthDateStatus() {
        return birthDateStatus;
    }
    public void setBirthDateStatus(int birthDateStatus) {
        this.birthDateStatus = birthDateStatus;
    }
    public int getCountryStatus() {
        return countryStatus;
    }
    public void setCountryStatus(int countryStatus) {
        this.countryStatus = countryStatus;
    }
    public String getCountryTruncated() {
        return getTruncatedValue(country, MAX_COUNTRY_LENGTH);
    }
    
    public int getEmployeeNumberStatus() {
        return employeeNumberStatus;
    }
    public void setEmployeeNumberStatus(int employeeNumberStatus) {
        this.employeeNumberStatus = employeeNumberStatus;
    }
    public int getStateOfResidenceStatus() {
        return stateOfResidenceStatus;
    }
    public void setStateOfResidenceStatus(int stateOfResidenceStatus) {
        this.stateOfResidenceStatus = stateOfResidenceStatus;
    }
    public int getEmployeeProvidedEmailStatus() {
        return employeeProvidedEmailStatus;
    }
    public void setEmployeeProvidedEmailStatus(int employeeProvidedEmailStatus) {
        this.employeeProvidedEmailStatus = employeeProvidedEmailStatus;
    }
    public int getNamePrefixStatus() {
        return namePrefixStatus;
    }
    public void setNamePrefixStatus(int namePrefixStatus) {
        this.namePrefixStatus = namePrefixStatus;
    }
    public int getPlanYTDHoursWorkedEffDateStatus() {
        return planYTDHoursWorkedEffDateStatus;
    }
    public void setPlanYTDHoursWorkedEffDateStatus(int planYTDHoursWorkedEffDateStatus) {
        this.planYTDHoursWorkedEffDateStatus = planYTDHoursWorkedEffDateStatus;
    }
    public int getPlanYTDHoursWorkedStatus() {
        return planYTDHoursWorkedStatus;
    }
    public void setPlanYTDHoursWorkedStatus(int planYTDHoursWorkedStatus) {
        this.planYTDHoursWorkedStatus = planYTDHoursWorkedStatus;
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
    public int getOptOutIndicatorStatus() {
        return optOutIndicatorStatus;
    }
    public void setOptOutIndicatorStatus(int optOutIndicatorStatus) {
        this.optOutIndicatorStatus = optOutIndicatorStatus;
    }
    public int getFirstNameStatus() {
        return firstNameStatus;
    }
    public void setFirstNameStatus(int firstNameStatus) {
        this.firstNameStatus = firstNameStatus;
    }
    public int getLastNameStatus() {
        return lastNameStatus;
    }
    public void setLastNameStatus(int lastNameStatus) {
        this.lastNameStatus = lastNameStatus;
    }
    public int getEmployeeStatusDateStatus() {
        return employeeStatusDateStatus;
    }
    public void setEmployeeStatusDateStatus(int employeeStatusDateStatus) {
        this.employeeStatusDateStatus = employeeStatusDateStatus;
    }
    public int getEmployeeStatusStatus() {
        return employeeStatusStatus;
    }
    public void setEmployeeStatusStatus(int employeeStatusStatus) {
        this.employeeStatusStatus = employeeStatusStatus;
    }
    public int getContractNumberStatus() {
        return contractNumberStatus;
    }
    public void setContractNumberStatus(int contractNumberStatus) {
        this.contractNumberStatus = contractNumberStatus;
    }
    public int getParticipantIdStatus() {
        return participantIdStatus;
    }
    public void setParticipantIdStatus(int participantIdStatus) {
        this.participantIdStatus = participantIdStatus;
    }
    public int getHireDateStatus() {
        return hireDateStatus;
    }
    public void setHireDateStatus(int hireDateStatus) {
        this.hireDateStatus = hireDateStatus;
    }
    public boolean isDoubleRowHeight() {
        return doubleRowHeight;
    }
    public void setDoubleRowHeight(boolean doubleRowHeight) {
        this.doubleRowHeight = doubleRowHeight;
    }
    public int getAnnualBaseSalaryStatus() {
        return annualBaseSalaryStatus;
    }
    public void setAnnualBaseSalaryStatus(int annualBaseSalaryStatus) {
        this.annualBaseSalaryStatus = annualBaseSalaryStatus;
    }
    public int getPlanYTDCompensationStatus() {
        return planYTDCompensationStatus;
    }
    public void setPlanYTDCompensationStatus(int planYTDCompensationStatus) {
        this.planYTDCompensationStatus = planYTDCompensationStatus;
    }
    public int getBeforeTaxDeferralAmtStatus() {
        return beforeTaxDeferralAmtStatus;
    }
    public void setBeforeTaxDeferralAmtStatus(int beforeTaxDeferralAmtStatus) {
        this.beforeTaxDeferralAmtStatus = beforeTaxDeferralAmtStatus;
    }
    public int getBeforeTaxDeferralPercStatus() {
        return beforeTaxDeferralPercStatus;
    }
    public void setBeforeTaxDeferralPercStatus(int beforeTaxDeferralPercStatus) {
        this.beforeTaxDeferralPercStatus = beforeTaxDeferralPercStatus;
    }
    public int getDesigRothDeferralAmtStatus() {
        return desigRothDeferralAmtStatus;
    }
    public void setDesigRothDeferralAmtStatus(int desigRothDeferralAmtStatus) {
        this.desigRothDeferralAmtStatus = desigRothDeferralAmtStatus;
    }
    public int getDesigRothDeferralPercStatus() {
        return desigRothDeferralPercStatus;
    }
    public void setDesigRothDeferralPercStatus(int desigRothDeferralPercStatus) {
        this.desigRothDeferralPercStatus = desigRothDeferralPercStatus;
    }
    public int getMiddleInitialStatus() {
        return middleInitialStatus;
    }
    public void setMiddleInitialStatus(int middleInitialStatus) {
        this.middleInitialStatus = middleInitialStatus;
    }
    public int getAddress1Status() {
        return address1Status;
    }
    public void setAddress1Status(int address1Status) {
        this.address1Status = address1Status;
    }
    public int getAddress2Status() {
        return address2Status;
    }
    public void setAddress2Status(int address2Status) {
        this.address2Status = address2Status;
    }
    public Date getVestedYearsOfServiceEffDate() {
        return vestedYearsOfServiceEffDate;
    }
    public void setVestedYearsOfServiceEffDate(Date previousYearsOfServiceEffDate) {
        this.vestedYearsOfServiceEffDate = previousYearsOfServiceEffDate;
    }
    public int getVestedYearsOfServiceEffDateStatus() {
        return vestedYearsOfServiceEffDateStatus;
    }
    public void setVestedYearsOfServiceEffDateStatus(
            int previousYearsOfServiceEffDateStatus) {
        this.vestedYearsOfServiceEffDateStatus = previousYearsOfServiceEffDateStatus;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public String getEligibilityDate() {
        return eligibilityDate;
    }
    public void setEligibilityDate(String eligibilityDate) {
        this.eligibilityDate = eligibilityDate;
    }
    public String getHireDate() {
        return hireDate;
    }
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    public String getAnnualBaseSalary() {
        return annualBaseSalary;
    }
    public void setAnnualBaseSalary(String annualBaseSalary) {
        this.annualBaseSalary = annualBaseSalary;
    }
    public int getPreviousYearsOfServiceStatus() {
        return previousYearsOfServiceStatus;
    }
    public void setPreviousYearsOfServiceStatus(int previousYearsOfServiceStatus) {
        this.previousYearsOfServiceStatus = previousYearsOfServiceStatus;
    }
    public String getBeforeTaxDeferralAmt() {
        return beforeTaxDeferralAmt;
    }
    public void setBeforeTaxDeferralAmt(String beforeTaxDeferralAmt) {
        this.beforeTaxDeferralAmt = beforeTaxDeferralAmt;
    }
    public String getBeforeTaxDeferralPerc() {
        return beforeTaxDeferralPerc;
    }
    public void setBeforeTaxDeferralPerc(String beforeTaxDeferralPerc) {
        this.beforeTaxDeferralPerc = beforeTaxDeferralPerc;
    }
    public String getDesigRothDeferralAmt() {
        return desigRothDeferralAmt;
    }
    public void setDesigRothDeferralAmt(String desigRothDeferralAmt) {
        this.desigRothDeferralAmt = desigRothDeferralAmt;
    }
    public String getDesigRothDeferralPerc() {
        return desigRothDeferralPerc;
    }
    public void setDesigRothDeferralPerc(String desigRothDeferralPerc) {
        this.desigRothDeferralPerc = desigRothDeferralPerc;
    }
    public String getEmployeeStatusDate() {
        return employeeStatusDate;
    }
    public void setEmployeeStatusDate(String employeeStatusDate) {
        this.employeeStatusDate = employeeStatusDate;
    }
    public String getPlanDate() {
        return planDate;
    }
    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }
    public String getPlanYTDCompensation() {
        return planYTDCompensation;
    }
    public void setPlanYTDCompensation(String planYTDCompensation) {
        this.planYTDCompensation = planYTDCompensation;
    }
    public String getPlanYTDHoursWorkedEffDate() {
        return planYTDHoursWorkedEffDate;
    }
    public void setPlanYTDHoursWorkedEffDate(String planYTDHoursWorkedEffDate) {
        this.planYTDHoursWorkedEffDate = planYTDHoursWorkedEffDate;
    }
    
    public static String getTruncatedValue(String value, int maxLength) {
        if (StringUtils.isNotEmpty(value) && value.length() > maxLength) {
            return value.substring(0, maxLength) + "...";
        }
        return value;
    }
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    public int getDivisionStatus() {
        return divisionStatus;
    }
    public void setDivisionStatus(int divisionStatus) {
        this.divisionStatus = divisionStatus;
    }

}