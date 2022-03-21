package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;

/**
 * ParticipantAccountDetailsVO class
 * This class is used as a value object used on the Participant Account page,
 * 
 * @author Simona Stoicescu
 * 
 **/

public class ParticipantAccountDetailsVO implements Serializable {
	
	BigDecimal 	profileId;
	String 		firstName;
	String 		lastName;
	String      middleInitial;
	String		ssn;
	Date		birthDate;
	int			age;
	String 		addressLine1;
	String 		addressLine2;
	String		cityName;
	String 		stateCode;
	String		zipCode;
	String 	    showLoanFeature;
	
	double 		totalAssets;
	double 		allocatedAssets;
	double 		loanAssets;
	double 		personalBrokerageAccount;
	
	String 		defaultInvestmentIndicator;
	String 		investmentInstructionType;
	Date   		lastContributionDate;
	String 		automaticRebalanceIndicator;
	String 		personalBrokerageAccountIndicator;
	String		employeeStatus;
	String		employmentStatus;						//CL 110234
	String		effectiveDate;							//CL 110234
    boolean     participantInd;
	
	boolean netEEDeferralContributionsAvailable = false;
	double netEEDeferralContributions;
	double maximumHardshipAmount;
	
	int		rothFirstDepositYear;
	boolean managedAccountServiceFeature =false;
	
	private ParticipantDeferralVO participantDeferralVO;
	
	//Gateway Phase 1 -- start
	//Participant gateway Indicator
    private String participantGatewayInd;
    private String participantGIFLIndicator;
	//Gateway Phase 1 -- end
//  Gateway Phase 1C -- start
    private String participantGIFLIndicatorAsSelect="No";
//  Gateway Phase 1C -- end
    
    private String managedAccountStatusValue;  
    
	private LifeIncomeAmountDetailsVO liaDetailsVO;

	/**
	 * Gets the profileId
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getProfileId() {
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(BigDecimal profileId) {
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
		return lastName;
	}
	/**
	 * Sets the lastName
	 * @param lastName The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleInitial(String middleInitial) 	{
		this.middleInitial = middleInitial;
	}
	
	public String getMiddleInitial() {
		return this.middleInitial;
	}
	
	// formatted name.
	public String getFullName() {
		if (this.middleInitial != null && this.middleInitial.trim().length() >0) {
			return this.lastName+", "+this.firstName + " " + this.middleInitial.trim();
		} else {
			return this.lastName+", "+this.firstName;
		}
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
	 * Gets the allocatedAssets
	 * @return Returns a double
	 */
	public double getAllocatedAssets() {
		return allocatedAssets;
	}
	/**
	 * Sets the allocatedAssets
	 * @param allocatedAssets The allocatedAssets to set
	 */
	public void setAllocatedAssets(double allocatedAssets) {
		this.allocatedAssets = allocatedAssets;
	}

	/**
	 * Gets the personalBrokerageAccount
	 * @return Returns a double
	 */
	public double getPersonalBrokerageAccount() {
		return personalBrokerageAccount;
	}
	/**
	 * Sets the personalBrokerageAccount
	 * @param personalBrokerageAccount The personalBrokerageAccount to set
	 */
	public void setPersonalBrokerageAccount(double personalBrokerageAccount) {
		this.personalBrokerageAccount = personalBrokerageAccount;
	}

	/**
	 * Gets the defaultInvestmentIndicator
	 * @return Returns a String
	 */
	public String getDefaultInvestmentIndicator() {
		return defaultInvestmentIndicator;
	}
	/**
	 * Sets the defaultInvestmentIndicator
	 * @param defaultInvestmentIndicator The defaultInvestmentIndicator to set
	 */
	public void setDefaultInvestmentIndicator(String defaultInvestmentIndicator) {
		this.defaultInvestmentIndicator = defaultInvestmentIndicator;
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
	 * Gets the lastContributionDate
	 * @return Returns a Date
	 */
	public Date getLastContributionDate() {
		return lastContributionDate;
	}
	/**
	 * Sets the lastContributionDate
	 * @param lastContributionDate The lastContributionDate to set
	 */
	public void setLastContributionDate(Date lastContributionDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastContributionDate);
		if(calendar.get(Calendar.YEAR) != 9999 && calendar.get(Calendar.YEAR) != 1)
		{
			this.lastContributionDate = lastContributionDate;
		}
	}

	/**
	 * Gets the automaticRebalance
	 * @return Returns a String
	 */
	public String getAutomaticRebalanceIndicator() {
		return automaticRebalanceIndicator;
	}
	/**
	 * Sets the automaticRebalanceIndicator
	 * @param automaticRebalanceIndicator The automaticRebalanceIndicator to set
	 */
	public void setAutomaticRebalanceIndicator(String automaticRebalance) {
		this.automaticRebalanceIndicator = automaticRebalance;
	}

	/**
	 * Gets the employeeStatus
	 * @return Returns a String
	 */
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	/**
	 * Sets the employeeStatus
	 * @param employeeStatus The employeeStatus to set
	 */
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
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
	 * Gets the addressLine1
	 * @return Returns a String
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * Sets the addressLine1
	 * @param addressLine1 The addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * Gets the addressLine2
	 * @return Returns a String
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * Sets the addressLine2
	 * @param addressLine2 The addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * Gets the cityName
	 * @return Returns a String
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * Sets the cityName
	 * @param cityName The cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Gets the stateCode
	 * @return Returns a String
	 */
	public String getStateCode() {
		return stateCode;
	}
	/**
	 * Sets the stateCode
	 * @param stateCode The stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * Gets the zipCode
	 * @return Returns a String
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * Sets the zipCode
	 * @param zipCode The zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Gets the loanAssets
	 * @return Returns a double
	 */
	public double getLoanAssets() {
		return loanAssets;
	}
	/**
	 * Sets the loanAssets
	 * @param loanAssets The loanAssets to set
	 */
	public void setLoanAssets(double loanAssets) {
		this.loanAssets = loanAssets;
	}

	/**
	 * Gets the showLoanFeature
	 * @return Returns a String
	 */
	public String getShowLoanFeature() {
		return showLoanFeature;
	}
	/**
	 * Sets the showLoanFeature
	 * @param showLoanFeature The showLoanFeature to set
	 */
	public void setShowLoanFeature(String showLoanFeature) {
		this.showLoanFeature = showLoanFeature;
	}

	/**
	 * Gets the personalBrokerageAccountIndicator
	 * @return Returns a String
	 */
	public String getPersonalBrokerageAccountIndicator() {
		return personalBrokerageAccountIndicator;
	}
	/**
	 * Sets the personalBrokerageAccountIndicator
	 * @param personalBrokerageAccountIndicator The personalBrokerageAccountIndicator to set
	 */
	public void setPersonalBrokerageAccountIndicator(String personalBrokerageAccountIndicator) {
		this.personalBrokerageAccountIndicator = personalBrokerageAccountIndicator;
	}

	/**
	 * @return Returns the netEEDeferralContributionsAvailable.
	 */
	public boolean isNetEEDeferralContributionsAvailable() {
		return netEEDeferralContributionsAvailable;
	}
	/**
	 * @param netEEDeferralContributionsAvailable The netEEDefeeralContributionsAvailable to set.
	 */
	public void setNetEEDeferralContributionsAvailable(boolean netEEDeferralContributionsAvailable) {
		this.netEEDeferralContributionsAvailable = netEEDeferralContributionsAvailable;
	}
	/**
	 * @return Returns the netEmployeeDeferralContibutions.
	 */
	public double getNetEEDeferralContributions() {
		return netEEDeferralContributions;
	}
	/**
	 * @param netEmployeeDeferralContributions The netEmployeeDeferralContributions to set.
	 */
	public void setNetEEDeferralContributions(double netEEDeferralContributions) {
		this.netEEDeferralContributions = netEEDeferralContributions;
	}
	/**
	 * @return Returns the maximumHardshipAmount.
	 */
	public double getMaximumHardshipAmount() {
		return maximumHardshipAmount;
	}
	/**
	 * @param maximumHardshipAmount The maximumHardshipAmount to set.
	 */
	public void setMaximumHardshipAmount(double maximumHardshipAmount) {
		this.maximumHardshipAmount = maximumHardshipAmount;
	}
	/**
	 * Gets the rothFirstDepositYear
	 * @return Returns an int
	 */
	public int getRothFirstDepositYear() {
		return rothFirstDepositYear;
	}
	/**
	 * Sets the rothFirstDepositYear
	 * @param rothFirstDepositYear The rothFirstDepositYear to set
	 */
	public void setRothFirstDepositYear(int rothFirstDepositYear) {
		this.rothFirstDepositYear = rothFirstDepositYear;
	}
    public boolean isParticipantInd() {
        return participantInd;
    }
    public void setParticipantInd(boolean participantInd) {
        this.participantInd = participantInd;
    }
	public ParticipantDeferralVO getParticipantDeferralVO() {
		return participantDeferralVO;
	}
	public void setParticipantDeferralVO(ParticipantDeferralVO participantDeferralVO) {
		this.participantDeferralVO = participantDeferralVO;
	}
	//Gateway Phase 1 -- start
	//Participant gateway Indicator
	 /**
	 * Gets the participantGatewayInd
	 * @return Returns String
	 */
    public String getParticipantGatewayInd() {
		return participantGatewayInd;
	}
    /**
	 * Sets the participantGatewayInd
	 * @param participantGatewayInd The participantGatewayInd to set
	 */
	public void setParticipantGatewayInd(String participantGatewayInd) {
		this.participantGatewayInd = participantGatewayInd;
	}
	
	/**
	 * Get the ParticipantGIFLIndicator
	 * @return the participantGIFLIndicator String 
	 */
	public String getParticipantGIFLIndicator() {

		if(("A".equals(getParticipantGatewayInd())) ||("D".equals(getParticipantGatewayInd()))){
			participantGIFLIndicator=participantGatewayInd;	
		}
		return participantGIFLIndicator;
	}
	/**
	 * Set the ParticipantGIFLIndicator
	 * @param participantGIFLIndicator 
	 */
	public void setParticipantGIFLIndicator(
			String participantGIFLIndicator) {
		this.participantGIFLIndicator = participantGIFLIndicator;
	}
	//Gateway Phase 1C -- start
	/**
	 * Get the ParticipantGIFLIndicator in terms of Yes/Deactivated/No.
	 * There is no setter property for this method.
	 * @return the participantGIFLIndicator String 
	 */
	public String getParticipantGIFLIndicatorAsSelect() {
		if ((getParticipantGatewayInd().equals("A"))) {
			participantGIFLIndicatorAsSelect = "Yes";
			if (isLiaEnabledForParticipant()) {
				participantGIFLIndicatorAsSelect = participantGIFLIndicatorAsSelect
						+ "/LIA";
			}
		} else if (getParticipantGatewayInd().equals("D")) {
			participantGIFLIndicatorAsSelect = "Deactivated";
		}
		return participantGIFLIndicatorAsSelect;
	}

	//	Gateway Phase 1C -- end
	//CL 110234 Begin
	/**
	 * Get the Employment Status
	 * @return the employmentStatus 
	 */
	public String getEmploymentStatus() {
		return employmentStatus;
	}
	
	/**
	 * Set the Employment Status
	 * @param employmentStatus 
	 */
	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	
	/**
	 * Get the Effective Date
	 * @return effectiveDate 
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	/**
	 * Set the Effective Date
	 * @param effectiveDate 
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	//CL 110234 End
	
	public LifeIncomeAmountDetailsVO getLiaDetailsVO() {
		return liaDetailsVO;
	}
	
	public void setLiaDetailsVO(LifeIncomeAmountDetailsVO liaDetailsVO) {
		this.liaDetailsVO = liaDetailsVO;
	}
	
	public boolean isGiflEnabledForParticipant() {
		return StringUtils.equals("Yes", getParticipantGIFLIndicatorAsSelect());
	}
	
	/**
	 * checks whether LIA is available for participant based on 
	 * valid anniversary date
	 *
	 * @return boolean
	 */
	public boolean isLiaEnabledForParticipant() {
		String date = liaDetailsVO.getFormatedAnniversaryDate();
		if (StringUtils.isNotBlank(date)
				&& !StringUtils.equals("9999-12-31", date)) {
			return true;
		}
		return false;
	}
	
	public String getManagedAccountStatusValue() {
		return managedAccountStatusValue;
	}

	public void setManagedAccountStatusValue(String managedAccountStatusValue) {
		this.managedAccountStatusValue = managedAccountStatusValue;
	}
}