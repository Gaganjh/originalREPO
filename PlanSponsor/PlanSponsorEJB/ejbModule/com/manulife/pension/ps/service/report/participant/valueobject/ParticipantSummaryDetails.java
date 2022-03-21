package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;

public class ParticipantSummaryDetails implements Serializable {
	
	/**
	 * Default serialVersionUID has been defined  
	 */
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	private String ssn;
	private String division;
	private int age;
	private Date birthDate;
	private String defaultBirthDate;
	private String status;
	private String defaultInvestment;
	private String investmentInstructionType;
	private String rothInd;
	private double employeeAssets;
	private double employerAssets;
	private double totalAssets;
	private double outstandingLoans;
	private String profileId;	
	private boolean showAge = true;
	private String participantGatewayInd;
	private String defaultGateway;
    private Date eligibilityDate;
    private LifeIncomeAmountDetailsVO liaDetails;
    private boolean showLIADetailsSection = false;
    private String employmentStatus; 							//CL 110234
	private String effectiveDate;								//CL 110234
	private String managedAccountStatusInd;  
	private String defaultManagedAccount;
	private static HashMap<String, String> statusDescMap;		//CL 110234
	static{														//CL 110234
		statusDescMap = new HashMap<String, String> ();			//CL 110234
		statusDescMap.put("All", "All");						//CL 110234
		statusDescMap.put("A", "Active");						//CL 110234
		statusDescMap.put("C", "Cancelled");					//CL 110234
		statusDescMap.put("D", "Deceased");						//CL 110234
		statusDescMap.put("P", "Disabled");						//CL 110234
		statusDescMap.put("R", "Retired");						//CL 110234
		statusDescMap.put("T", "Terminated");					//CL 110234
		statusDescMap.put("n/a", "n/a");						//CL 110234

	}
	
	public ParticipantSummaryDetails() {}
	
	public ParticipantSummaryDetails( 	String firstName,
							  		  	String lastName, 
							  		 	String ssn,
							  		 	String division,
							  		 	int age,
							  		 	String defaultBirthDate,
							  		 	Date birthDate,
							  			String status,
							  			String defaultInvestment,
							  			String investmentInstructionType,
							  			String rothInd,
							  			double employeeAssets, 
							  			double employerAssets,
							  			double totalAssets, 
							  			double outstandingLoans,
							  			String profileId)
	{
		this.firstName = firstName;
		this.lastName = lastName;		
		this.age = age;
		this.ssn = ssn;
		this.division = division;
		this.defaultBirthDate = defaultBirthDate;
		this.birthDate = birthDate;
		this.status = status;
		this.defaultInvestment = defaultInvestment;
		this.investmentInstructionType = investmentInstructionType;
		this.rothInd = rothInd;
		this.employeeAssets = employeeAssets;
		this.employerAssets = employerAssets;
		this.totalAssets = totalAssets;
		this.outstandingLoans = outstandingLoans;
		this.profileId = profileId;
		
	}
	
	//Gateway phase 1 -- start
	//Adding gateway indicators to constructor
	public ParticipantSummaryDetails( 	String firstName,
							  		  	String lastName, 
							  		 	String ssn,
							  		 	String division,
							  		 	int age,
							  		 	String defaultBirthDate,
							  		 	Date birthDate,
							  			String status,
							  			String defaultInvestment,
							  			String investmentInstructionType,
							  			String rothInd,
							  			double employeeAssets, 
							  			double employerAssets,
							  			double totalAssets, 
							  			double outstandingLoans,
							  			String profileId,
							  			String participantGatewayInd,							  		
							  			String defaultGateway, 
							  			String managedAccountStatusInd,         
                                        String defaultManagedAccount
							  			)
	{
		this.firstName = firstName;
		this.lastName = lastName;		
		this.age = age;
		this.ssn = ssn;
		this.division = division;
		this.defaultBirthDate = defaultBirthDate;
		this.birthDate = birthDate;
		this.status = status;
		this.defaultInvestment = defaultInvestment;
		this.investmentInstructionType = investmentInstructionType;
		this.rothInd = rothInd;
		this.employeeAssets = employeeAssets;
		this.employerAssets = employerAssets;
		this.totalAssets = totalAssets;
		this.outstandingLoans = outstandingLoans;
		this.profileId = profileId;
		this.participantGatewayInd = participantGatewayInd;
		this.defaultGateway = defaultGateway;
		this.managedAccountStatusInd = managedAccountStatusInd;
		this.defaultManagedAccount = defaultManagedAccount;
	}
	//Gateway phase 1 -- end
    
//  Gateway phase 1 -- start
    //Adding gateway indicators to constructor
    public ParticipantSummaryDetails(   String firstName,
                                        String lastName, 
                                        String ssn,
                                        String division,
                                        int age,
                                        String defaultBirthDate,
                                        Date birthDate,
                                        String status,
                                        String defaultInvestment,
                                        String investmentInstructionType,
                                        String rothInd,
                                        double employeeAssets, 
                                        double employerAssets,
                                        double totalAssets, 
                                        double outstandingLoans,
                                        String profileId,
                                        String participantGatewayInd,                                      
                                        String defaultGateway,
                                        String managedAccountStatusInd,         
                                        String defaultManagedAccount,
                                        Date eligibilityDate
                                        )
    {
        this(firstName, lastName, ssn, division, age, defaultBirthDate, birthDate, status, defaultInvestment,investmentInstructionType, rothInd,
             employeeAssets, employerAssets, totalAssets, outstandingLoans, profileId, participantGatewayInd, 
             defaultGateway, managedAccountStatusInd,defaultManagedAccount);
        setEligibilityDate(eligibilityDate);
    }    
    
    //CL 110234 Begin
    //Adding new args{employmentStatus, effectiveDate}
    public ParticipantSummaryDetails(   String firstName,
            String lastName, 
            String ssn,
            String division,
            int age,
            String defaultBirthDate,
            Date birthDate,
            String status,
            String defaultInvestment,
            String investmentInstructionType,
            String rothInd,
            double employeeAssets, 
            double employerAssets,
            double totalAssets, 
            double outstandingLoans,
            String profileId,
            String participantGatewayInd,         
            String defaultGateway,
            String managedAccountStatusInd,         
            String defaultManagedAccount,
            Date eligibilityDate,  
            String employmentStatus, 
            String effectiveDate
            )
{
this(firstName, lastName, ssn, division, age, defaultBirthDate, birthDate, status, defaultInvestment,investmentInstructionType, rothInd,
employeeAssets, employerAssets, totalAssets, outstandingLoans, profileId, participantGatewayInd, 
defaultGateway, managedAccountStatusInd, defaultManagedAccount, eligibilityDate);
setEmploymentStatus(employmentStatus);
setEffectiveDate(effectiveDate);
}
//CL 110234 End   
	/**
	 * For some participants we don't have the correct birth date, 
	 * and therefore we shouldn't show their age.
	 * @return Returns a boolean
	 */
	public boolean getShowAge() {
		return showAge;
	}


	/**
	 * For some participants we don't have the correct birth date, 
	 * and therefore we shouldn't show their age.
	 */
	public void setShowAge(boolean showAge) {
		this.showAge = showAge;
	}



	/**
	 * Gets the profile id
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}
	
	/**
	 * Sets the profile id
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
				
	/**
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the lastName
	 * @return Returns a String
	 */
    public String getLastName() {        
        if(lastName != null) {
            return lastName.trim();
        } else {
            return null;
        }       
    }
	/**
	 * Sets the lastName
	 * @param lastName The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the ssn
	 * @return Returns a String
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * Sets the ssn
	 * @param ssn The ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * Gets the division
	 * @return Returns a String
	 */
	public String getDivision() {
		return division;
	}
	/**
	 * Sets the division
	 * @param division The division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}
	
	/**
	 * Gets the age
	 * @return Returns a int
	 */
	public int getAge() {
		return age;
	}
	/**
	 * Sets the age
	 * @param age The age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets the status
	 * @return Returns a String
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Sets the status
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the defaultInvestment
	 * @return Returns a String
	 */
	public String getDefaultInvestment() {
		return defaultInvestment;
	}
	/**
	 * Sets the defaultInvestment
	 * @param defaultInvestment The defaultInvestment to set
	 */
	public void setDefaultInvestment(String defaultInvestment) {
		this.defaultInvestment = defaultInvestment;
	}
	
	
	/**
	 * Gets the investmentInstructionType
	 * @return Returns a String
	 */
	public String getInvestmentInstructionType() {
		return investmentInstructionType;
	}
	/**
	 * Sets the investmentInstructionType
	 * @param investmentInstructionType The investmentInstructionType to set
	 */
	public void setInvestmentInstructionType(String investmentInstructionType) {
		this.investmentInstructionType = investmentInstructionType;
	}

	/**
	 * Gets the rothMoney
	 * @return Returns a String
	 */
	public String getRothInd() {
		return rothInd;
	}
	/**
	 * Sets the rothMoney
	 * @param rothMoney The rothMoney to set
	 */
	public void setRothInd(String rothInd) {
		this.rothInd = rothInd;
	}	
	
	
	/**
	 * Gets the employeeAssets
	 * @return Returns a double
	 */
	public double getEmployeeAssets() {
		return employeeAssets;
	}
	/**
	 * Sets the employeeAssets
	 * @param employeeAssets The employeeAssets to set
	 */
	public void setEmployeeAssets(double employeeAssets) {
		this.employeeAssets = employeeAssets;
	}

	/**
	 * Gets the employerAssets
	 * @return Returns a double
	 */
	public double getEmployerAssets() {
		return employerAssets;
	}
	/**
	 * Sets the employerAssets
	 * @param employerAssets The employerAssets to set
	 */
	public void setEmployerAssets(double employerAssets) {
		this.employerAssets = employerAssets;
	}

	/**
	 * Gets the totalAssets
	 * @return Returns a double
	 */
	public double getTotalAssets() {
		return totalAssets;
	}
	/**
	 * Sets the totalAssets
	 * @param totalAssets The totalAssets to set
	 */
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}

	/**
	 * Gets the outstandingLoans
	 * @return Returns a double
	 */
	public double getOutstandingLoans() {
		return outstandingLoans;
	}
	/**
	 * Sets the outstandingLoans
	 * @param outstandingLoans The outstandingLoans to set
	 */
	public void setOutstandingLoans(double outstandingLoans) {
		this.outstandingLoans = outstandingLoans;
	}

	/**
	 * Gets the birthDate
	 * @return Returns a Date
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * Sets the birthDate
	 * @param birthDate The birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Gets the default birth date display string.
	 * @return String - The default birth date display string.
	 */
	public String getDefaultBirthDate() {
		return defaultBirthDate;
	}
	/**
	 * Sets the default birth date display string.
	 * @param defaultBirthDate The default birth date display string to set.
	 */
	public void setDefaultBirthDate(String defaultBirthDate) {
		this.defaultBirthDate = defaultBirthDate;
	}

	/**
	 * Gets the Participant Gateway display string.
	 * @return String - The Participant Gateway display string.
	 */
	public String getParticipantGatewayInd() {
		return participantGatewayInd;
	}
	/**
	 * Sets the participant Gateway display string.
	 * @param participantGateway The participant Gateway display string to set.
	 */
	public void setParticipantGatewayInd(String participantGatewayInd) {
		this.participantGatewayInd = participantGatewayInd;
	}
	/**
	 * Gets the defaultGateway display string.
	 * @return String - The defaultGateway display string.
	 */
	public String getDefaultGateway() {
		return defaultGateway;
	}
	/**
	 * Sets the defaultGateway display string.
	 * @param defaultGateway The defaultGateway display string to set.
	 */
	public void setDefaultGateway(String defaultGateway) {
		this.defaultGateway = defaultGateway;
	}

    /**
     * @return the eligibilityDate
     */
    public Date getEligibilityDate() {
        return eligibilityDate;
    }

    /**
     * @param eligibilityDate the eligibilityDate to set
     */
    public void setEligibilityDate(Date eligibilityDate) {
        this.eligibilityDate = eligibilityDate;
    }
    
  //CL 110234 Begin
    /**
     * @return the employmentStatus
     */
    public String getEmploymentStatus() {
		return employmentStatus;
	}
    
    /**
     * @return the employment Status Description
     */
    public String getEmploymentStatusDescription() {
		return statusDescMap.get(employmentStatus);
	}

	/**
	 * Sets the EmploymentStatus display string.
	 * @param employmentStatus
	 */
	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	
    /**
     * @return the effectiveDate
     */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the setEffectiveDate display string.
	 * @param effectiveDate
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	//CL 110234 End

	/**
	 * @return
	 */
	public LifeIncomeAmountDetailsVO getLiaDetails() {
		return liaDetails;
	}

	/**
	 * @param liaDetails
	 */
	public void setLiaDetails(LifeIncomeAmountDetailsVO liaDetails) {
		this.liaDetails = liaDetails;
	}
	
	/**
	 * @return
	 */
	public boolean isShowLIADetailsSection() {
		return showLIADetailsSection;
	}

	/**
	 * @param showLIADetailsSection
	 */
	public void setShowLIADetailsSection(boolean showLIADetailsSection) {
		this.showLIADetailsSection = showLIADetailsSection;
	}
	
	/**
	 * Gets the Managed Account display string.
	 * @return String - The Participant Managed Account display string.
	 */
    public String getManagedAccountStatusInd() {
		return managedAccountStatusInd;
	}
    /**
	 * Sets the participant Managed Account display string.
	 * @param participantManagedAccount The participant Managed Account display string to set.
	 */
	public void setManagedAccountStatusInd(String managedAccountStatusInd) {
		this.managedAccountStatusInd = managedAccountStatusInd;
	}
	/**
	 * Sets the defaultManagedAccount display string.
	 * @param defaultManagedAccount The defaultManagedAccount display string to set.
	 */
	public String getDefaultManagedAccount() {
		return defaultManagedAccount;
	}
	/**
	 * Sets the defaultManagedAccount display string.
	 * @param defaultManagedAccount The defaultManagedAccount display string to set.
	 */
	public void setDefaultManagedAccount(String defaultManagedAccount) {
		this.defaultManagedAccount = defaultManagedAccount;
	}
		
}