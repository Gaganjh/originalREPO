package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.validator.ValidationError;

/**
 * This action form stores the common information related to view/add/edit
 * employee snapshot page.
 * 
 * @author guweigu
 * 
 */
public class EmployeeSnapshotForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3523386309368005869L;

	private static Set<String> basicSectionFields = new HashSet<String>();

	private static Set<String> employmentSectionFields = new HashSet<String>();

	private static Set<String> contactSectionFields = new HashSet<String>();

	private static Set<String> participationSectionFields = new HashSet<String>();
	
	
	static {
		basicSectionFields.add(EmployeeSnapshotProperties.FirstName);
		basicSectionFields.add(EmployeeSnapshotProperties.LastName);
		basicSectionFields.add(EmployeeSnapshotProperties.SSN);
		basicSectionFields.add(EmployeeSnapshotProperties.Prefix);
		basicSectionFields.add(EmployeeSnapshotProperties.EmployeeId);
		basicSectionFields.add(EmployeeSnapshotProperties.BirthDate);

		employmentSectionFields.add(EmployeeSnapshotProperties.HireDate);
		employmentSectionFields.add(EmployeeSnapshotProperties.Division);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.EmploymentStatus);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.EmploymentStatusEffectiveDate);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.AnnualBaseSalary);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.PlanYearToDateComp);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.YearToDatePlanHoursEffDate);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.YearToDatePlanHoursWorked);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.MaskSensitiveInformation);
		employmentSectionFields
				.add(EmployeeSnapshotProperties.MaskSensitiveInformationComment);

		contactSectionFields.add(EmployeeSnapshotProperties.AddressLine1);
		contactSectionFields.add(EmployeeSnapshotProperties.AddressLine2);
		contactSectionFields.add(EmployeeSnapshotProperties.City);
		contactSectionFields.add(EmployeeSnapshotProperties.State);
		contactSectionFields.add(EmployeeSnapshotProperties.Country);
		contactSectionFields.add(EmployeeSnapshotProperties.ZipCode);
		contactSectionFields.add(EmployeeSnapshotProperties.StateOfResidence);
		contactSectionFields.add(EmployeeSnapshotProperties.EmailAddress);

		participationSectionFields
				.add(EmployeeSnapshotProperties.EligibilityInd);
		participationSectionFields
				.add(EmployeeSnapshotProperties.EligibilityDate);
		participationSectionFields.add(EmployeeSnapshotProperties.OptOut);
		participationSectionFields.add(EmployeeSnapshotProperties.Ae90DaysOptOut);
		participationSectionFields
				.add(EmployeeSnapshotProperties.BeforeTaxDefPer);
		participationSectionFields
				.add(EmployeeSnapshotProperties.BeforeTaxFlatDef);
		participationSectionFields
				.add(EmployeeSnapshotProperties.DesignatedRothDefPer);
		participationSectionFields
				.add(EmployeeSnapshotProperties.DesignatedRothDefAmt);
		
		
		
	}
	private static final VestingMoneyTypeComparator moneyTypeComparator = new VestingMoneyTypeComparator();

	/**
	 * The source page id from where the user comes to this page
	 */
	protected String source = CensusConstants.CENSUS_SUMMARY_PAGE;

	protected boolean expandBasic = true;

	protected boolean expandContact = false;

	protected boolean expandEmployment = false;

	protected boolean expandParticipation = false;
    
    protected boolean expandVesting = false;

	protected Boolean showCensusHistoryValue = null;

	protected String profileId;

	protected boolean viewSalary = false;


	private List<MoneyTypeVO> moneyTypes =  new ArrayList<MoneyTypeVO>();

	private Date planYearEnd;

	/**
	 * an indicator to tell if the navigation is from ViewEmployeeSnapshot
	 */
	protected boolean fromView = false;
	
	protected boolean fromReset = false;
	
	protected boolean fromEdit = false;

	private boolean showVesting = false;

	private boolean vestingCollected = false;
	
	private boolean hosBasedVesting = false;
	
	private Boolean partialParticipantStatus;
	
	//Online Beneficiary To be displayed or not
	protected boolean onlineBeneficiaryLinkDisplayed = false;

	//AE 90 days opt out ind
	protected boolean ae90DaysOptOutDisplayed = false;
	protected boolean showConfirmationToDo = false;
	
	private boolean eligibilityCalcCsfOn=false;
	private ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes= new ArrayList<EligibilityCalculationMoneyType>();
	private ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypes= new ArrayList<EligibilityCalculationMoneyType>();
	private boolean pendingEligibilityCalculationRequest=false;
	
	// For LTPT Assessment Year
	private int longTermPartTimeAssessmentYear;
	private boolean displayLongTermPartTimeAssessmentYearField;
	
	public boolean isShowConfirmationToDo() {
		return showConfirmationToDo;
	}

	public void setShowConfirmationToDo(boolean showConfirmationToDo) {
		this.showConfirmationToDo = showConfirmationToDo;
	}


	public boolean isFromEdit() {
		return fromEdit;
	}

	public void setFromEdit(boolean fromEdit) {
		this.fromEdit = fromEdit;
	}

	public boolean isFromView() {
		return fromView;
	}

	public void setFromView(boolean fromView) {
		this.fromView = fromView;
	}
	
	public boolean isFromReset() {
		return fromReset;
	}

	public void setFromReset(boolean fromReset) {
		this.fromReset = fromReset;
	}
	
	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
    
    public boolean isShowVestingLink() {
        return (showVesting && !vestingCollected && !MoneyTypeVO
                .getAreAllMoneyTypesFullyVested(moneyTypes));
    }

	public boolean isExpandBasic() {
		return expandBasic;
	}

	public void setExpandBasic(boolean expandBasic) {
		this.expandBasic = expandBasic;
	}

	public boolean isExpandContact() {
		return expandContact;
	}

	public void setExpandContact(boolean expandContact) {
		this.expandContact = expandContact;
	}

	public boolean isExpandEmployment() {
		return expandEmployment;
	}

	public void setExpandEmployment(boolean expandEmployment) {
		this.expandEmployment = expandEmployment;
	}

	public boolean isExpandParticipation() {
		return expandParticipation;
	}

	public void setExpandParticipation(boolean expandParticipation) {
		this.expandParticipation = expandParticipation;
	}

	public Boolean isShowCensusHistoryValue() {
		return showCensusHistoryValue;
	}

	public void setShowCensusHistoryValue(Boolean showCensusHistoryValue) {
		this.showCensusHistoryValue = showCensusHistoryValue;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void reset( HttpServletRequest request) {
		expandBasic = true;
		expandContact = false;
		expandEmployment = false;
		expandParticipation = false;
		profileId = null;
		fromView = false;
		showCensusHistoryValue = false;
		source = CensusConstants.CENSUS_SUMMARY_PAGE;
		action = null;
		viewSalary = false;
		showConfirmationToDo = false;
		if (this.getEligibilityServiceMoneyTypes() != null) {
			int totalNumberOfMoneyTypes = this
			.getEligibilityServiceMoneyTypes().size();

	for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
		EligibilityCalculationMoneyType currentECalcMoneyType = this
				.getEligibilityServiceMoneyTypes().get(i);
		 currentECalcMoneyType.setCalculationOverride(null);
		
	}
	}
	}

	/**
	 * Set the section expanding indicator based on field errors.
	 * 
	 * @param errors
	 */
	public void expandOnErrors(List<ValidationError> errors) {
		Set<String> fields = new HashSet<String>();
		for (ValidationError error : errors) {
			fields.addAll(error.getFieldIds());
		}
		// if the fields set contains any field in basicSectionFields,
		// then removeAll will return true
		if (fields.removeAll(basicSectionFields)) {
			expandBasic = true;
		}
		if (fields.removeAll(employmentSectionFields)) {
			expandEmployment = true;
		}
		if (fields.removeAll(contactSectionFields)) {
			expandContact = true;
		}
		if (fields.removeAll(participationSectionFields)) {
			expandParticipation = true;
		}
		
		
	}

	public boolean isViewSalary() {
		return viewSalary;
	}

	public void setViewSalary(boolean viewSalary) {
		this.viewSalary = viewSalary;
	}

	public boolean isHosBasedVesting() {
		return hosBasedVesting;
	}

	public void setHosBasedVesting(boolean hosBasedVesting) {
		this.hosBasedVesting = hosBasedVesting;
	}

	public boolean isShowVesting() {
		return showVesting;
	}

	public void setShowVesting(boolean showVesting) {
		this.showVesting = showVesting;
	}
	
	/**
	 * Give the form a chance to react for any errors on the form
	 * @param errors
	 */
	protected void onErrors(List<ValidationError> errors) {
		return;
	}
	
	public List<MoneyTypeVO> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<MoneyTypeVO> moneyTypes) {
		this.moneyTypes = moneyTypes;
		Collections.sort(this.moneyTypes, moneyTypeComparator);
	}

	public Date getPlanYearEnd() {
		return planYearEnd;
	}

	public void setPlanYearEnd(Date planYearEnd) {
		this.planYearEnd = planYearEnd;
	}

	public boolean isVestingCollected() {
		return vestingCollected;
	}

	public void setVestingCollected(boolean vestingCollected) {
		this.vestingCollected = vestingCollected;
	}

    public Boolean getPartialParticipantStatus() {
        return partialParticipantStatus;
    }

    public void setPartialParticipantStatus(Boolean partialParticipantStatus) {
        this.partialParticipantStatus = partialParticipantStatus;
    }
	
    public boolean isAe90DaysOptOutDisplayed() {
		return ae90DaysOptOutDisplayed;
	}

	public void setAe90DaysOptOutDisplayed(boolean ae90DaysOptOutDisplayed) {
		this.ae90DaysOptOutDisplayed = ae90DaysOptOutDisplayed;
	}

    
    /**
	 * Check if ae opt out sould be displayed
	 *
	 * @return
	 */
	public boolean displayAe90DaysOptOut(Integer contractId) throws SystemException{
		boolean displayed = false;
		
		try{
			ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();  
			ContractServiceFeature csf = delegate.getContractServiceFeature(
					contractId, ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			if (csf != null && ServiceFeatureConstants.YES.equals(csf.getValue())) {
				PlanDataLite planDataLite = delegate.getPlanDataLight(contractId);
				if(planDataLite != null && ServiceFeatureConstants.YES.equals(
						planDataLite.getIsNinetyDayOrShorterWithdrawalElectionOffered())){
					displayed = true;
				}
			} 
		}catch (ApplicationException e) {
			throw new SystemException(e,"Error thrown while calling csf for " + contractId);
		}

		return displayed;
	}

	/**
	 * @return the onlineBeneficiaryLinkDisplayed
	 */
	public boolean isOnlineBeneficiaryLinkDisplayed() {
		return onlineBeneficiaryLinkDisplayed;
	}

	/**
	 * @param onlineBeneficiaryLinkDisplayed the onlineBeneficiaryLinkDisplayed to set
	 */
	public void setOnlineBeneficiaryLinkDisplayed(
			boolean onlineBeneficiaryLinkDisplayed) {
		this.onlineBeneficiaryLinkDisplayed = onlineBeneficiaryLinkDisplayed;
	}
	
	/**
	 * Check if Online Beneficiary should be displayed
	 *
	 * @return
	 */
	public boolean displayBeneficiaryLinkChoose(Integer contractId) throws SystemException{
		boolean displayed = false;
		try{
			ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
			ContractServiceFeature csf = delegate.getContractServiceFeature(
					contractId, ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);
			if ((csf != null) && ServiceFeatureConstants.YES.equals(csf.getValue())) {
				displayed = true;
			}

		}catch (ApplicationException e) {
			throw new SystemException(e,"Error thrown while calling csf for " + contractId);
		}
		return displayed;
	}
	
    public boolean isExpandVesting() {
        return expandVesting;
    }

    public void setExpandVesting(boolean expandVesting) {
        this.expandVesting = expandVesting;
    }

	public boolean isEligibilityCalcCsfOn() {
		return eligibilityCalcCsfOn;
	}

	public void setEligibilityCalcCsfOn(boolean eligibilityCalcCsfOn) {
		this.eligibilityCalcCsfOn = eligibilityCalcCsfOn;
	}

	public ArrayList<EligibilityCalculationMoneyType> getEligibilityServiceMoneyTypes() {
		return eligibilityServiceMoneyTypes;
	}

	public void setEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes) {
		this.eligibilityServiceMoneyTypes = eligibilityServiceMoneyTypes;
	}
	
	/**
	 * Struts uses this method.
	 */
	public void setEligibityServiceMoneyTypeId(int index, 
			EligibilityCalculationMoneyType ecMoneyType) {

		this.eligibilityServiceMoneyTypes.add(index, ecMoneyType);

	}

	/**
	 * Struts uses this method.
	 * @param index
	 * @return
	 */
	public EligibilityCalculationMoneyType getEligibityServiceMoneyTypeId(
			int index) {

		return eligibilityServiceMoneyTypes.get(index);

	}

	public ArrayList<EligibilityCalculationMoneyType> getLastUpdatedEligibilityServiceMoneyTypes() {
		return lastUpdatedEligibilityServiceMoneyTypes;
	}

	public void setLastUpdatedEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypes) {
		this.lastUpdatedEligibilityServiceMoneyTypes = lastUpdatedEligibilityServiceMoneyTypes;
	}

	public boolean isPendingEligibilityCalculationRequest() {
		return pendingEligibilityCalculationRequest;
	}

	public void setPendingEligibilityCalculationRequest(
			boolean pendingEligibilityCalculationRequest) {
		this.pendingEligibilityCalculationRequest = pendingEligibilityCalculationRequest;
	}
	
	/**
	 * Get the set of the name of field that has changed
	 *
	 * @return
	 */
	public Set<String> getEligibilityMoneyTypeFieldNames() {
		Set<String> changedSet = new HashSet<String>();
		
		if (isEligibilityCalcCsfOn()) {

			List<EligibilityCalculationMoneyType> latest = getEligibilityServiceMoneyTypes();

			int index = 0;

			for (EligibilityCalculationMoneyType moneyType : latest) {
				EligibilityCalculationMoneyType latestMoneyType = latest
						.get(index);
				
				changedSet.add(latestMoneyType.getMoneyTypeId() + ":"
							+ "eligibityServiceMoneyTypeId[" + index
							+ "].eligibilityDate");
			
				index++;

			}
		}

		return changedSet;
	}
	public int getLongTermPartTimeAssessmentYear() {
		return longTermPartTimeAssessmentYear;
	}

	public void setLongTermPartTimeAssessmentYear(int longTermPartTimeAssessmentYear) {
		this.longTermPartTimeAssessmentYear = longTermPartTimeAssessmentYear;
	}
	
	public boolean isDisplayLongTermPartTimeAssessmentYearField() {
		return displayLongTermPartTimeAssessmentYearField;
	}

	public void setDisplayLongTermPartTimeAssessmentYearField(boolean displayLongTermPartTimeAssessmentYearField) {
		this.displayLongTermPartTimeAssessmentYearField = displayLongTermPartTimeAssessmentYearField;
	}
	
}
