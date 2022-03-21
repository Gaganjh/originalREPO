package com.manulife.pension.ps.web.pif;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * PIFDataUi is the bean for convert the value object type to user interface bean type 
 * while displaying the plan information data to the user and the reverse while submitting form. 
 * @author 	rajenra
 */
public class PIFDataUi extends BaseSerializableCloneableObject{

    /**
     * Default serialVersionUID.
     */
	private static final long serialVersionUID = 1L;
	
    public static final String MEDIUM_MD_SLASHED = "MM/dd"; 
    
    public static final String YES = "Yes";

    public static final String NO = "No";
    
    public static final String YES_CODE = "Y";
    
    public static final String NO_CODE = "N";

    public static final String UNSPECIFIED = "Unspecified";    
    
	private String employerTaxIdentificationNumber;

    private String planEffectiveDate;
    
    private String planYearEndString;

    private String firstPlanEntryDateString;
    
    private String automaticEnrollmentDate;
    
    private String aciEffectiveDate;
    
    private String aciApplyDate;
    
    private String aciAppliesToEffectiveDate;    
    
    private String deferralMaxPercent;
    
    private String deferralMinPercent;
    
    private String annualIncrease;
    
    private String maxAutomaticIncrease;    
    
    private String[] employerRulesets = new String[100];

    private String[] viewableRules;
    
    private String[] selectedDeferralElectionMonths = ArrayUtils.EMPTY_STRING_ARRAY;    
    
	private String[] selectedWithdrawalReasons = ArrayUtils.EMPTY_STRING_ARRAY;
    
    private String[] selectedUnvestedMoneyOptions = ArrayUtils.EMPTY_STRING_ARRAY;
    
    private boolean hasBothEEDEFAndEEROTMoneyTypes;
    
    private String deferralPercentageForAutomaticEnrollment;
    
    private String partialWithdrawalMinimumWithdrawalAmount;
    
    private String maximumAmortizationPeriodGeneralPurpose;

    private String maximumAmortizationPeriodHardship;

    private String maximumAmortizationPeriodPrimaryResidence;
    
    private String minimumLoanAmount;

    private String maximumLoanAmount;

    private String maximumLoanPercentage;
    
    private String maximumNumberOfOutstandingLoans;

    private String loanInterestRateAbovePrime;   
    
	private List<PIFMoneyType> employerMoneyTypeList = new ArrayList<PIFMoneyType>();
	
    private boolean save;
    
    //The initialPlanInfoVO property is included here to compare the selected money type in money type tab 
    //with the initial money types available in the PIFAllowableMoneyTypesVO list. While saving the submission plan 
    //add unselected money types only if it is available in the initial PIFAllowableMoneyTypesVO list while removing 
    //all the unselected money type from the final list.
    private PlanInfoVO initialPlanInfoVO;
    
	/**
     * Default Constructor.
     */
    public PIFDataUi() {
        setSelectedWithdrawalReasons(new String[0]);
        setSelectedUnvestedMoneyOptions(new String[0]);   
    }
  
    /**
	 * @return the employerTaxIdentificationNumber
	 */
	public String getEmployerTaxIdentificationNumber() {
		return employerTaxIdentificationNumber;
	}

	/**
	 * @param employerTaxIdentificationNumberPart1 the employerTaxIdentificationNumber to set
	 */
	public void setEmployerTaxIdentificationNumber(
			String employerTaxIdentificationNumber) {
		this.employerTaxIdentificationNumber = employerTaxIdentificationNumber;
	}

	/**
	 * @return the planEffectiveDate
	 */
	public String getPlanEffectiveDate() {
		return planEffectiveDate;
	}



	/**
	 * @param planEffectiveDate the planEffectiveDate to set
	 */
	public void setPlanEffectiveDate(String planEffectiveDate) {
		this.planEffectiveDate = planEffectiveDate;
	}



	/**
	 * @return the firstPlanEntryDateString
	 */
	public String getFirstPlanEntryDateString() {
		return firstPlanEntryDateString;
	}



	/**
	 * @param firstPlanEntryDateString the firstPlanEntryDateString to set
	 */
	public void setFirstPlanEntryDateString(String firstPlanEntryDateString) {
		this.firstPlanEntryDateString = firstPlanEntryDateString;
	}	
	
	/**
	 * @return the aciEffectiveDate
	 */
	public String getAciEffectiveDate() {
		return aciEffectiveDate;
	}



	/**
	 * @param aciEffectiveDate the aciEffectiveDate to set
	 */
	public void setAciEffectiveDate(String aciEffectiveDate) {
		this.aciEffectiveDate = aciEffectiveDate;
	}



	/**
	 * @return the aciApplyDate
	 */
	public String getAciApplyDate() {
		return aciApplyDate;
	}



	/**
	 * @param aciApplyDate the aciApplyDate to set
	 */
	public void setAciApplyDate(String aciApplyDate) {
		this.aciApplyDate = aciApplyDate;
	}



	/**
	 * @return the aciAppliesToEffectiveDate
	 */
	public String getAciAppliesToEffectiveDate() {
		return aciAppliesToEffectiveDate;
	}



	/**
	 * @param aciAppliesToEffectiveDate the aciAppliesToEffectiveDate to set
	 */
	public void setAciAppliesToEffectiveDate(String aciAppliesToEffectiveDate) {
		this.aciAppliesToEffectiveDate = aciAppliesToEffectiveDate;
	}



	/**
	 * @return the deferralMaxPercent
	 */
	public String getDeferralMaxPercent() {
		return deferralMaxPercent;
	}



	/**
	 * @param deferralMaxPercent the deferralMaxPercent to set
	 */
	public void setDeferralMaxPercent(String deferralMaxPercent) {
		this.deferralMaxPercent = deferralMaxPercent;
	}



	/**
	 * @return the deferralMinPercent
	 */
	public String getDeferralMinPercent() {
		return deferralMinPercent;
	}



	/**
	 * @param deferralMinPercent the deferralMinPercent to set
	 */
	public void setDeferralMinPercent(String deferralMinPercent) {
		this.deferralMinPercent = deferralMinPercent;
	}

	/**
	 * @return the employerRulesets
	 */
	public String[] getEmployerRulesets() {
		return employerRulesets;
	}



	/**
	 * @param employerRulesets the employerRulesets to set
	 */
	public void setEmployerRulesets(String[] employerRulesets) {
		this.employerRulesets = employerRulesets;
	}

	/**
	 * @return the viewableRules
	 */
	public String[] getViewableRules() {
		return viewableRules;
	}

	/**
	 * @param viewableRules the viewableRules to set
	 */
	public void setViewableRules(String[] viewableRules) {
		this.viewableRules = viewableRules;
	}
	
    /**
	 * @return the selectedDeferralElectionMonths
	 */
	public String[] getSelectedDeferralElectionMonths() {
		return selectedDeferralElectionMonths;
	}

	/**
	 * @param selectedDeferralElectionMonths the selectedDeferralElectionMonths to set
	 */
	public void setSelectedDeferralElectionMonths(
			String[] selectedDeferralElectionMonths) {
		this.selectedDeferralElectionMonths = selectedDeferralElectionMonths;
	}	

	/**
	 * @return the automaticEnrollmentDate
	 */
	public String getAutomaticEnrollmentDate() {
		return automaticEnrollmentDate;
	}

	/**
	 * @param automaticEnrollmentDate the automaticEnrollmentDate to set
	 */
	public void setAutomaticEnrollmentDate(String automaticEnrollmentDate) {
		this.automaticEnrollmentDate = automaticEnrollmentDate;
	}

	/**
	 * @return the selectedWithdrawalReasons
	 */
	public String[] getSelectedWithdrawalReasons() {
		return selectedWithdrawalReasons;
	}

	/**
	 * @param selectedWithdrawalReasons the selectedWithdrawalReasons to set
	 */
	public void setSelectedWithdrawalReasons(String[] selectedWithdrawalReasons) {
		this.selectedWithdrawalReasons = selectedWithdrawalReasons;
	}

	/**
	 * @return the selectedUnvestedMoneyOptions
	 */
	public String[] getSelectedUnvestedMoneyOptions() {
		return selectedUnvestedMoneyOptions;
	}

	/**
	 * @param selectedUnvestedMoneyOptions the selectedUnvestedMoneyOptions to set
	 */
	public void setSelectedUnvestedMoneyOptions(
			String[] selectedUnvestedMoneyOptions) {
		this.selectedUnvestedMoneyOptions = selectedUnvestedMoneyOptions;
	}

	/**
	 * @return the hasBothEEDEFAndEEROTMoneyTypes
	 */
	public boolean isHasBothEEDEFAndEEROTMoneyTypes() {
		return hasBothEEDEFAndEEROTMoneyTypes;
	}

	/**
	 * @param hasBothEEDEFAndEEROTMoneyTypes the hasBothEEDEFAndEEROTMoneyTypes to set
	 */
	public void setHasBothEEDEFAndEEROTMoneyTypes(
			boolean hasBothEEDEFAndEEROTMoneyTypes) {
		this.hasBothEEDEFAndEEROTMoneyTypes = hasBothEEDEFAndEEROTMoneyTypes;
	}

	/**
	 * @return the deferralPercentageForAutomaticEnrollment
	 */
	public String getDeferralPercentageForAutomaticEnrollment() {
		return deferralPercentageForAutomaticEnrollment;
	}

	/**
	 * @param deferralPercentageForAutomaticEnrollment the deferralPercentageForAutomaticEnrollment to set
	 */
	public void setDeferralPercentageForAutomaticEnrollment(
			String deferralPercentageForAutomaticEnrollment) {
		this.deferralPercentageForAutomaticEnrollment = deferralPercentageForAutomaticEnrollment;
	}

	/**
	 * @return the partialWithdrawalMinimumWithdrawalAmount
	 */
	public String getPartialWithdrawalMinimumWithdrawalAmount() {
		return partialWithdrawalMinimumWithdrawalAmount;
	}

	/**
	 * @param partialWithdrawalMinimumWithdrawalAmount the partialWithdrawalMinimumWithdrawalAmount to set
	 */
	public void setPartialWithdrawalMinimumWithdrawalAmount(
			String partialWithdrawalMinimumWithdrawalAmount) {
		this.partialWithdrawalMinimumWithdrawalAmount = partialWithdrawalMinimumWithdrawalAmount;
	}

	/**
	 * @return the maximumAmortizationPeriodGeneralPurpose
	 */
	public String getMaximumAmortizationPeriodGeneralPurpose() {
		return maximumAmortizationPeriodGeneralPurpose;
	}

	/**
	 * @param maximumAmortizationPeriodGeneralPurpose the maximumAmortizationPeriodGeneralPurpose to set
	 */
	public void setMaximumAmortizationPeriodGeneralPurpose(
			String maximumAmortizationPeriodGeneralPurpose) {
		this.maximumAmortizationPeriodGeneralPurpose = maximumAmortizationPeriodGeneralPurpose;
	}

	/**
	 * @return the maximumAmortizationPeriodHardship
	 */
	public String getMaximumAmortizationPeriodHardship() {
		return maximumAmortizationPeriodHardship;
	}

	/**
	 * @param maximumAmortizationPeriodHardship the maximumAmortizationPeriodHardship to set
	 */
	public void setMaximumAmortizationPeriodHardship(
			String maximumAmortizationPeriodHardship) {
		this.maximumAmortizationPeriodHardship = maximumAmortizationPeriodHardship;
	}

	/**
	 * @return the maximumAmortizationPeriodPrimaryResidence
	 */
	public String getMaximumAmortizationPeriodPrimaryResidence() {
		return maximumAmortizationPeriodPrimaryResidence;
	}

	/**
	 * @param maximumAmortizationPeriodPrimaryResidence the maximumAmortizationPeriodPrimaryResidence to set
	 */
	public void setMaximumAmortizationPeriodPrimaryResidence(
			String maximumAmortizationPeriodPrimaryResidence) {
		this.maximumAmortizationPeriodPrimaryResidence = maximumAmortizationPeriodPrimaryResidence;
	}

	/**
	 * @return the minimumLoanAmount
	 */
	public String getMinimumLoanAmount() {
		return minimumLoanAmount;
	}

	/**
	 * @param minimumLoanAmount the minimumLoanAmount to set
	 */
	public void setMinimumLoanAmount(String minimumLoanAmount) {
		this.minimumLoanAmount = minimumLoanAmount;
	}

	/**
	 * @return the maximumLoanAmount
	 */
	public String getMaximumLoanAmount() {
		return maximumLoanAmount;
	}

	/**
	 * @param maximumLoanAmount the maximumLoanAmount to set
	 */
	public void setMaximumLoanAmount(String maximumLoanAmount) {
		this.maximumLoanAmount = maximumLoanAmount;
	}

	/**
	 * @return the maximumLoanPercentage
	 */
	public String getMaximumLoanPercentage() {
		return maximumLoanPercentage;
	}

	/**
	 * @param maximumLoanPercentage the maximumLoanPercentage to set
	 */
	public void setMaximumLoanPercentage(String maximumLoanPercentage) {
		this.maximumLoanPercentage = maximumLoanPercentage;
	}

	/**
	 * @return the loanInterestRateAbovePrime
	 */
	public String getLoanInterestRateAbovePrime() {
		return loanInterestRateAbovePrime;
	}

	/**
	 * @param loanInterestRateAbovePrime the loanInterestRateAbovePrime to set
	 */
	public void setLoanInterestRateAbovePrime(String loanInterestRateAbovePrime) {
		this.loanInterestRateAbovePrime = loanInterestRateAbovePrime;
	}

	/**
	 * @return the employerMoneyTypeList
	 */
	public List<PIFMoneyType> getEmployerMoneyTypeList() {
		return employerMoneyTypeList;
	}

	/**
	 * @param employerMoneyTypeList the employerMoneyTypeList to set
	 */
	public void setEmployerMoneyTypeList(List<PIFMoneyType> employerMoneyTypeList) {
		this.employerMoneyTypeList = employerMoneyTypeList;
	}
	
    /**
	 * @return the save
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * @param save the save to set
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

	/**
	 * @return the planYearEndString
	 */
	public String getPlanYearEndString() {
		return planYearEndString;
	}

	/**
	 * @param planYearEndString the planYearEndString to set
	 */
	public void setPlanYearEndString(String planYearEndString) {
		this.planYearEndString = planYearEndString;
	}

	/**
	 * @return the initialPlanInfoVO
	 */
	public PlanInfoVO getInitialPlanInfoVO() {
		return initialPlanInfoVO;
	}

	/**
	 * @param initialPlanInfoVO the initialPlanInfoVO to set
	 */
	public void setInitialPlanInfoVO(PlanInfoVO initialPlanInfoVO) {
		this.initialPlanInfoVO = initialPlanInfoVO;
	}

	/**
	 * @return the annualIncrease
	 */
	public String getAnnualIncrease() {
		return annualIncrease;
	}

	/**
	 * @param annualIncrease the annualIncrease to set
	 */
	public void setAnnualIncrease(String annualIncrease) {
		this.annualIncrease = annualIncrease;
	}

	/**
	 * @return the maxAutomaticIncrease
	 */
	public String getMaxAutomaticIncrease() {
		return maxAutomaticIncrease;
	}

	/**
	 * @param maxAutomaticIncrease the maxAutomaticIncrease to set
	 */
	public void setMaxAutomaticIncrease(String maxAutomaticIncrease) {
		this.maxAutomaticIncrease = maxAutomaticIncrease;
	}

	/**
	 * @return the maximumNumberOfOutstandingLoans
	 */
	public String getMaximumNumberOfOutstandingLoans() {
		return maximumNumberOfOutstandingLoans;
	}

	/**
	 * @param maximumNumberOfOutstandingLoans the maximumNumberOfOutstandingLoans to set
	 */
	public void setMaximumNumberOfOutstandingLoans(
			String maximumNumberOfOutstandingLoans) {
		this.maximumNumberOfOutstandingLoans = maximumNumberOfOutstandingLoans;
	}
}
