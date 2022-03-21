package com.manulife.pension.ps.web.contract.csf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.ps.web.contract.csf.util.CsfUtil;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;


/**
 * Form Bean Class to hold the CSF values for CSF pages
 * @author Puttaiah Arugunta
 * 
 */
@SuppressWarnings("unchecked")
public class CsfForm extends CloneableAutoForm {

    private static final long serialVersionUID = 2964084625095100166L;

    // Value Objects to persist the data for the CSF View Page. 
    private ParticipantServicesData participantServicesData;
    private PlanSponsorServicesData planSponsorServicesData;


    //Address management CSF values
    private String addressChanges[] = {};

    /*Manage Deferral Election CSF values*/
    //Participants can specify deferral amounts as
    private String deferralType;
    //Participants are allowed to change their deferrals online
    private String changeDeferralsOnline; 
    private String deferralLimitPercent;
    private String deferralLimitDollars;
    private String deferralMaxLimitPercent;
    private String deferralMaxLimitDollars;
    //Participants are allowed to enroll online
    private String enrollOnline;
    //Payroll cut off for online deferral and auto enrollment changes
    private String payrollCutoff; 

    /* Financial Transactions Section*/
    //Participants can initiate inter-account transfer online
    private String particiapntIATs;
    //Participants can initiate online loans
    private String participantInitiateLoansInd;
    //Participants can initiate i:withdrawal requests
    private String participantWithdrawalInd;


    /*Plan Support Services*/
    //Plan Highlights are created by John Hancock
    private String summaryPlanHighlightAvailable;
    private String summaryPlanHighlightReviewed;
    
    private boolean displayNoticeGeneration;

    //Eligibility Calculation values
    private String eligibilityCalculationInd="";
    private ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes= new ArrayList();
    private ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypes= new ArrayList();
    private ArrayList<EligibilityCalculationMoneyType> selectedEligibilityServiceMoneyTypes= new ArrayList();
    private ArrayList<EligibilityCalculationMoneyType> contractMoneyTypes= new ArrayList();
    private List<MoneyTypeVO> contractMoneyTypesList = new ArrayList<MoneyTypeVO>();
    private boolean showEligibilitySection= false;   
    private int eligibilityServiceMoneyTypesListSize=0;
    private List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria=new ArrayList();
    private Map<String, Date> moneyTypesWithFutureDatedDeletionDate = new HashMap<String, Date>();
    // Property to hold the <form:multibox> values for the EC section
    private String[] selectedMoneyTypes = {};

    // Auto Enrollment CSF values
    private String autoEnrollInd;
    //Initial enrollment date for the EZstart service is
    private String initialEnrollmentDate;
    // Direct Mail
    private String directMailInd;

    // ACI csf values
    private String autoContributionIncrease;
    //First scheduled increase starts on properties [yyyy]
    private String aciAnniversaryDate;
    private String aciAnniversaryYear;
    //Initial increase to take effect on first anniversary date after the enrollment
    private String increaseAnniversary;
    // Hidden property to hold the Auto/Signup Value in the Form
    private String aciSignupMethod;

    /*Vesting Section*/
    //Vesting will be
    private String vestingPercentagesMethod;
    //Reporting vesting percentages on participant statements
    private String vestingDataOnStatement;

    //  Plan Frequency
    private String planFrequency;

    // Auto Payroll CSF values
    private String autoPayrollInd;
    
    //  Payroll Feedback Service
    private String payrollFeedbackService;

    //Consent to deliver the Contract and any subsequent amendments to the contract via plan sponsor website
    private String consentInd="consentInd";
    private boolean isHideConsent ;

    // loans CSF 
    private String allowOnlineLoans;
    private String loansChecksMailedTo;
    private String whoWillReviewLoanRequests;
    private String creatorMayApproveLoanRequestsInd;
    private String allowLoansPackageToGenerate;

    // Property to determine whether to show Loans section or not in CSF page
    private String loanRecordKeepingInd;
    // property to hold outstatanding Loan count
    private int outstandingOldILoanRequestCount = 0;

    // Withdrawal CSF values
    private String withdrawalInd;
    private String specialTaxNotice;
    private String checksMailedTo;
    private String whoWillReviewWithdrawals;
    private String creatorMayApproveInd;
    private String onlineWithdrawalProcess;

    // Property to hold whether TPA firm exists or not for the contract
    private boolean tpaFirmExists = false;

    // Support properties
    private boolean editing = false;
    private String button = null;
    private boolean isFreezePeriod = false;
    private boolean isDeferralEZiDisabled = false;
    private boolean editable = false;
    private String planAciInd;
    private String planDeferralLimitPercent;
    private String planDeferralMaxLimitPercent;
    
    // Property to support select all money types by default when Turn On EC service.
    private String checkEligibilityCalculationInd;

    private String resetDeferralValues;

    private boolean ipsServiceSuppressed = true;
    
    // Online Beneficiary Designation Service properties.
    private String onlineBeneficiaryInd;
    
    //Co-Fiduciary indicator
    private boolean coFiduciary;
    private boolean coFidFeatureSuppressed = true;
    private String selectedInvestmentProfile;
    
	//Managed Account sub-section
  	private boolean planEligibleForManagedAccounts;
  	private ManagedAccountServiceFeatureLite managedAccountServiceFeature;
  	private boolean managedAccountSectionEditable;
	private String managedAccountServiceAvailableToPptDate;
    
    
    //Notice Service
    private String noticeServiceInd;
    private String noticeTypeSelected;
    private ArrayList<CoFidServiceFeatureDetails> coFidServiceFeatureDetails=new ArrayList<CoFidServiceFeatureDetails>();
    
	// edelivery for plan notices and statements
	private String wiredAtWork;
	private String noticeOfInternetAvailability;
	// used to show/hide the Notices Edelivery section in Edit/Confirm and view pages
	private boolean showNoticesEdeliveryInEditPage;
	private boolean showNoticesEdeliveryInViewPage;

	public boolean isShowNoticesEdeliveryInEditPage() {
		return showNoticesEdeliveryInEditPage;
	}

	public void setShowNoticesEdeliveryInEditPage(boolean showNoticesEdeliveryInEditPage) {
		this.showNoticesEdeliveryInEditPage = showNoticesEdeliveryInEditPage;
	}

	public String getWiredAtWork() {
		return wiredAtWork;
	}

	public void setWiredAtWork(String wiredAtWork) {
		this.wiredAtWork = wiredAtWork;
	}

	public String getNoticeOfInternetAvailability() {
		return noticeOfInternetAvailability;
	}

	public void setNoticeOfInternetAvailability(String noticeOfInternetAvailability) {
		this.noticeOfInternetAvailability = noticeOfInternetAvailability;
	}

     
	public ArrayList<CoFidServiceFeatureDetails> getCoFidServiceFeatureDetails() {
		return coFidServiceFeatureDetails;
	}

	public void setCoFidServiceFeatureDetails(
			ArrayList<CoFidServiceFeatureDetails> coFidServiceFeatureDetails) {
		this.coFidServiceFeatureDetails = coFidServiceFeatureDetails;
	}
	
	/**
	 * @return the addressChanges
	 */
	public String[] getAddressChanges() {
		return addressChanges;
	}

	/**
	 * @param addressChanges the addressChanges to set
	 */
	public void setAddressChanges(String[] addressChanges) {
		this.addressChanges = addressChanges;
	}

	/**
	 * @return the isDeferralEZiDisabled
	 */
	public boolean getIsDeferralEZiDisabled() {
		return isDeferralEZiDisabled;
	}

	/**
	 * @param isDeferralEZiDisabled the isDeferralEZiDisabled to set
	 */
	public void setDeferralEZiDisabled(boolean isDeferralEZiDisabled) {
		this.isDeferralEZiDisabled = isDeferralEZiDisabled;
	}

	/**
	 * @return the selectedMoneyTypes
	 */
	public String[] getSelectedMoneyTypes() {
		return selectedMoneyTypes;
	}

	/**
	 * @param selectedMoneyTypes the selectedMoneyTypes to set
	 */
	public void setSelectedMoneyTypes(String[] selectedMoneyTypes) {
		this.selectedMoneyTypes = selectedMoneyTypes;
	}

	/**
     * Use the cloned version of the form to revert any changes (i.e. if the user Cancels any
     * updates)
     */
    public void revert() {
        CsfForm oldForm = (CsfForm) getClonedForm();
        
        if (oldForm != null) {
          
        	this.setVestingPercentagesMethod(oldForm.getVestingPercentagesMethod());
            this.setVestingDataOnStatement(oldForm.getVestingDataOnStatement());
            // revert withdrawals values
            this.setWithdrawalInd(oldForm.getWithdrawalInd());
            this.setCreatorMayApproveInd(oldForm.getCreatorMayApproveInd());
            this.setOnlineWithdrawalProcess(oldForm.getOnlineWithdrawalProcess());
            this.setSpecialTaxNotice(oldForm.getSpecialTaxNotice());
            this.setParticipantWithdrawalInd(oldForm.getParticipantWithdrawalInd());
            this.setWhoWillReviewWithdrawals(oldForm.getWhoWillReviewWithdrawals());
            
            // revert auto payroll values
            this.setAutoPayrollInd(oldForm.getAutoPayrollInd());

            // revert auto enroll values
            this.setAutoEnrollInd(oldForm.getAutoEnrollInd());
            this.setDirectMailInd (oldForm.getDirectMailInd());
            this.setPlanFrequency(oldForm.getPlanFrequency());
            this.setInitialEnrollmentDate(oldForm.getInitialEnrollmentDate());
            // revert PAM
            this.setAddressChanges(oldForm.getAddressChanges());
            
            // deferral
            this.setDeferralType(oldForm.getDeferralType());
            this.setDeferralLimitDollars(oldForm.getDeferralLimitDollars());
            this.setDeferralLimitPercent(oldForm.getDeferralLimitPercent());
            this.setChangeDeferralsOnline(oldForm.getChangeDeferralsOnline());
            this.setEnrollOnline(oldForm.getEnrollOnline());
            
            // aci
            this.setAutoContributionIncrease(oldForm.getAutoContributionIncrease());
            this.setAciAnniversaryDate(oldForm.getAciAnniversaryDate());
            this.setAciAnniversaryYear(oldForm.getAciAnniversaryYear());
            this.setAciSignupMethod(oldForm.getAciSignupMethod());
            this.setIncreaseAnniversary(oldForm.getIncreaseAnniversary());
            this.setIsFreezePeriod(oldForm.getIsFreezePeriod());
            //loans
            this.setAllowOnlineLoans(oldForm.getAllowOnlineLoans());
            this.setParticipantInitiateLoansInd(oldForm.getParticipantInitiateLoansInd());
            this.setWhoWillReviewLoanRequests(oldForm.getWhoWillReviewLoanRequests());
            this.setCreatorMayApproveLoanRequestsInd(oldForm.getCreatorMayApproveLoanRequestsInd());
            this.setLoansChecksMailedTo(oldForm.getLoansChecksMailedTo());
            this.setAllowLoansPackageToGenerate(oldForm.getAllowLoansPackageToGenerate());
            
            //revert Consent Information values
            this.setConsentInd(oldForm.getConsentInd());
            
           //revert Eligiblity Calculation vlaues
           this.setEligibilityCalculationInd(oldForm.getEligibilityCalculationInd());
                                 

            // onboarding
            this.setSummaryPlanHighlightAvailable(oldForm.getSummaryPlanHighlightAvailable());
            
            
            
        }
    }
    


/**
 * This method keeps a copy of last updated eligibility service money types,
 * which is used to compare and check if the user has edited any value or
 * not.
 *
 * @param lastUpdatedEligibilityServiceMoneyTypeList
 * @return ArrayList
 */
public ArrayList copyLastUpdatedEligibityServiceMoneyTypes(
		ArrayList lastUpdatedEligibilityServiceMoneyTypeList) {
	ArrayList cloneEligibilityServiceMoneyTypeList = new ArrayList();
	Iterator lastUpdatedListIterator = lastUpdatedEligibilityServiceMoneyTypeList
			.iterator();
	while (lastUpdatedListIterator.hasNext()) {
		EligibilityCalculationMoneyType eligibilityCalculationMoneyType = new EligibilityCalculationMoneyType();
		EligibilityCalculationMoneyType lastUpdated = (EligibilityCalculationMoneyType) lastUpdatedListIterator
				.next();
		eligibilityCalculationMoneyType.setMoneyTypeDescription(lastUpdated
				.getMoneyTypeDescription());
		eligibilityCalculationMoneyType.setMoneyTypeName(lastUpdated
				.getMoneyTypeName());
		eligibilityCalculationMoneyType.setMoneyTypeId(lastUpdated.getMoneyTypeId());
		eligibilityCalculationMoneyType.setMoneyTypeValue(lastUpdated
				.getMoneyTypeValue());
		eligibilityCalculationMoneyType.setFieldName(lastUpdated
				.getFieldName());
		cloneEligibilityServiceMoneyTypeList
				.add(eligibilityCalculationMoneyType);
	}
	return cloneEligibilityServiceMoneyTypeList;
}
    /**
     * @return Returns the editing.
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * @param editing The editing to set.
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     * @return Returns the editing.
     */
    public String getEditMode() {
        return "" + editing;
    }

    /**
     * @param editing The editing to set.
     */
    public void setEditMode(String editMode) {
        setEditing(editMode != null && editMode.equalsIgnoreCase("true"));
    }

    /**
     * @return the button
     */
    public String getButton() {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(String button) {
        this.button = button;
    }

    /**
     * @return String
     */
    public String getVestingPercentagesMethod() {
        return vestingPercentagesMethod;
    }

    /**
     * @param vestingPercentagesToBeCalculated
     */
    public void setVestingPercentagesMethod(String vestingPercentagesMethod) {
        this.vestingPercentagesMethod = vestingPercentagesMethod;
        // force it to false since the input is disabled, it may not
        // get in the HTTPRequest
        if (CsfConstants.PLAN_VESTING_NA.equals(vestingPercentagesMethod)) {
        	setVestingDataOnStatement(CsfConstants.CSF_NO);
        }
    }

    /**
     * @return String
     */
    public String getVestingDataOnStatement() {
        return vestingDataOnStatement;
    }

    /**
     * @param vestingDataOnPSW
     */
    public void setVestingDataOnStatement(String vestingDataOnStatement) {
        this.vestingDataOnStatement = vestingDataOnStatement;
    }

    public String getAutoEnrollInd() {
        return autoEnrollInd;
    }

    public void setAutoEnrollInd(String autoEnrollInd) {
        this.autoEnrollInd = autoEnrollInd;
    }

    public String getAutoPayrollInd() {
        return autoPayrollInd;
    }

    public void setAutoPayrollInd(String autoPayrollInd) {
        this.autoPayrollInd = autoPayrollInd;
    }

    public String getCreatorMayApproveInd() {
        return creatorMayApproveInd;
    }

    public void setCreatorMayApproveInd(String creatorMayApproveInd) {
        this.creatorMayApproveInd = creatorMayApproveInd;
    }

    public String getInitialEnrollmentDate() {
        return initialEnrollmentDate;
    }

    public void setInitialEnrollmentDate(String initialEnrollmentDate) {
        this.initialEnrollmentDate = initialEnrollmentDate;
    }

    public String getOnlineWithdrawalProcess() {
        return onlineWithdrawalProcess;
    }

    public void setOnlineWithdrawalProcess(String onlineWithdrawalProcess) {
    	this.onlineWithdrawalProcess = onlineWithdrawalProcess;
    }


    public String getPlanFrequency() {
        return planFrequency;
    }

    public void setPlanFrequency(String planFrequency) {
        this.planFrequency = planFrequency;
    }

    public String getWithdrawalInd() {
        return withdrawalInd;
    }

    public void setWithdrawalInd(String withdrawalInd) {
        this.withdrawalInd = withdrawalInd;
    }
    
    /**
     * Method to detect whether the given string is presenet or not in the array list
     * @param a
     * @return
     */
	 private boolean hasAddressCode(String a){
		 boolean isFound = false;
		 for(int i=0;i < this.addressChanges.length; i++){
			 if(a.equals(this.addressChanges[i])){
				 isFound = true;
			 }
		 }
		 return isFound;
	 }

    public String getEnrollOnline() {
		return enrollOnline;
	}

	public void setEnrollOnline(String enrollOnline) {
		this.enrollOnline = enrollOnline;
	}

	public String getChangeDeferralsOnline() {
		return changeDeferralsOnline;
	}

	public void setChangeDeferralsOnline(String deferralChangeOnline) {
		this.changeDeferralsOnline = deferralChangeOnline;
	}
	
	public String getActiveAddress() {
    	 return hasAddressCode(ServiceFeatureConstants.ADDRESS_MANAGEMENT_ACTIVE)? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO; 
    }
	
	public String getDisabledAddress() {
		return hasAddressCode(ServiceFeatureConstants.ADDRESS_MANAGEMENT_DISABLED)? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO; 
    }

    public String getRetiredAddress() {
    	return hasAddressCode(ServiceFeatureConstants.ADDRESS_MANAGEMENT_RETIRED)? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO; 
    }

    public String getTerminatedAddress() {
    	return hasAddressCode(ServiceFeatureConstants.ADDRESS_MANAGEMENT_TERMINATED)? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO; 
    }

    public String getAddressManagementView() {
        StringBuffer sf = new StringBuffer();
        if (ServiceFeatureConstants.YES.equals(getActiveAddress()) && ServiceFeatureConstants.YES.equals(getTerminatedAddress()) &&
        		ServiceFeatureConstants.YES.equals(getRetiredAddress()) && ServiceFeatureConstants.YES.equals(getDisabledAddress())) {

            return CsfConstants.ADDRESS_MANAGEMENT_ALL;
        }
        if (ServiceFeatureConstants.NO.equals(getActiveAddress()) && ServiceFeatureConstants.NO.equals(getTerminatedAddress()) &&
        		ServiceFeatureConstants.NO.equals(getRetiredAddress()) && ServiceFeatureConstants.NO.equals(getDisabledAddress())) {

            return CsfConstants.ADDRESS_MANAGEMENT_NONE;
        }

        if (ServiceFeatureConstants.YES.equals(getActiveAddress())) {
            sf.append(CsfConstants.ADDRESS_MANAGEMENT_ACTIVE).append(",");
        }
        if (ServiceFeatureConstants.YES.equals(getTerminatedAddress())) {
            sf.append(CsfConstants.ADDRESS_MANAGEMENT_TERMINATED).append(",");;
        }
        if (ServiceFeatureConstants.YES.equals(getRetiredAddress())) {
            sf.append(CsfConstants.ADDRESS_MANAGEMENT_RETIRED).append(",");;
        }
        if (ServiceFeatureConstants.YES.equals(getDisabledAddress())) {
            sf.append(CsfConstants.ADDRESS_MANAGEMENT_DISABLED).append(",");;
        }
        // if last char is "," remove it.
        if (sf.lastIndexOf(",") == sf.length() - 1)
            sf.deleteCharAt(sf.length() - 1);

        return sf.toString().replace(",", ", ");

    }
    
	/**
	 * Method to get the Initial Enrollment Date in the String format.
	 * This method will be invoked by using reflection API. 
	 * 
	 * 
	 * @param csfForm
	 * @return date in String format
	 */
	public String getInitialEnrollmentDateAsString(){
		
		return CsfUtil.convertDateFormat(this.getInitialEnrollmentDate(),
				CsfConstants.aciDisplayDateFormat, CsfConstants.aciDBDateFormat);
	
	}
	
	/**
	 * Method to get the Anniversary Date in String format
	 * This method will be invoked by using reflection API. 
	 * 
	 * @param csfForm
	 * @return date in String format
	 */
	public String getAnniversaryDateAsString(){
		//First scheduled increase starts on [yyyy]
		return CsfUtil.convertDateFormat(this.getAciAnniversaryDate()+"/"+this.getAciAnniversaryYear(),
				CsfConstants.aciDisplayDateFormat, CsfConstants.aciDBDateFormat);
	}

    public void removeLeadingZeros()
    {
    	this.deferralLimitPercent = removeLeadingZeros(deferralLimitPercent);
    	this.deferralLimitDollars = removeLeadingZeros(deferralLimitDollars);
    }
    
    private String removeLeadingZeros(String amount)
	{
		if(amount != null && amount.trim().length() > 0)
		{
			while(amount.startsWith("0"))
			{
				amount = amount.substring(1);
			}
		}
		return amount;
	}

    public String getChecksMailedTo() {
        return checksMailedTo;
    }

    public void setChecksMailedTo(String checksMailedTo) {
        this.checksMailedTo = checksMailedTo;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getParticipantWithdrawalInd() {
        return participantWithdrawalInd;
    }
    public void setParticipantWithdrawalInd(String participantWithdrawalInd) {
        this.participantWithdrawalInd = participantWithdrawalInd;
    }

	public String getDeferralLimitDollars() {
		return deferralLimitDollars;
	}

	public void setDeferralLimitDollars(String deferralLimitDollars) {
		this.deferralLimitDollars = deferralLimitDollars;
	}

	public String getDeferralLimitPercent() {
		return deferralLimitPercent;
	}

	public void setDeferralLimitPercent(String deferralLimitPercent) {
		this.deferralLimitPercent = deferralLimitPercent;
	}

	public String getDeferralType() {
		return deferralType;
	}

	public void setDeferralType(String deferralType) {
		this.deferralType = deferralType;
	}

	public String getAciAnniversaryDate() {
		return aciAnniversaryDate;
	}

	public void setAciAnniversaryDate(String aciAnniversaryDate) {
		this.aciAnniversaryDate = aciAnniversaryDate;
	}

	public String getAciAnniversaryYear() {
		return aciAnniversaryYear;
	}

	public void setAciAnniversaryYear(String aciAnniversaryYear) {
		this.aciAnniversaryYear = aciAnniversaryYear;
	}

	public String getAciSignupMethod() {
		return aciSignupMethod;
	}

	public void setAciSignupMethod(String aciSignupMethod) {
		this.aciSignupMethod = aciSignupMethod;
	}

	public String getAutoContributionIncrease() {
		return autoContributionIncrease;
	}

	public void setAutoContributionIncrease(String autoContributionIncrease) {
		this.autoContributionIncrease = autoContributionIncrease;
	}



	public String getIncreaseAnniversary() {
		return increaseAnniversary;
	}

	public void setIncreaseAnniversary(String increaseAnniversary) {
		this.increaseAnniversary = increaseAnniversary;
	}



	public boolean getIsFreezePeriod() {
		return isFreezePeriod;
	}

	public void setIsFreezePeriod(boolean isFreezePeriod) {
		this.isFreezePeriod = isFreezePeriod;
	}

	/**
	 * @return the whoWillReviewWithdrawals
	 */
	public String getWhoWillReviewWithdrawals() {
		return whoWillReviewWithdrawals;
	}

	/**
	 * @param whoWillReviewWithdrawals the whoWillReviewWithdrawals to set
	 */
	public void setWhoWillReviewWithdrawals(String whoWillReviewWithdrawals) {
		this.whoWillReviewWithdrawals = whoWillReviewWithdrawals;
		// if the value of this CSF is No Review then set the
		// IsReviewStageRequiredCSF to 'N'
		if (CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW
				.equalsIgnoreCase(whoWillReviewWithdrawals)) {
			setOnlineWithdrawalProcess(CsfConstants.CSF_NO);
		}
		// if the value of this CSF is PS or TPA then set the
		// IsReviewStageRequiredCSF to 'Y'
		else if ((CsfConstants.WHO_WILL_REVIEW_WD_PS
				.equalsIgnoreCase(whoWillReviewWithdrawals))
				|| (CsfConstants.WHO_WILL_REVIEW_WD_TPA
						.equalsIgnoreCase(whoWillReviewWithdrawals))) {
			setOnlineWithdrawalProcess(CsfConstants.CSF_YES);
		}
	}

	/**
	 * @return the allowOnlineLoans
	 */
	public String getAllowOnlineLoans() {
		return allowOnlineLoans;
	}

	/**
	 * @param allowOnlineLoans the allowOnlineLoans to set
	 */
	public void setAllowOnlineLoans(String allowOnlineLoans) {
		this.allowOnlineLoans = allowOnlineLoans;
	}

	/**
	 * @return the participantInitiateLoansInd
	 */
	public String getParticipantInitiateLoansInd() {
		return participantInitiateLoansInd;
	}

	/**
	 * @param participantInitiateLoansInd the participantInitiateLoansInd to set
	 */
	public void setParticipantInitiateLoansInd(String participantInitiateLoansInd) {
		this.participantInitiateLoansInd = participantInitiateLoansInd;
	}

	/**
	 * @return the whoWillReviewLoanRequests
	 */
	public String getWhoWillReviewLoanRequests() {
		return whoWillReviewLoanRequests;
	}

	/**
	 * @param whoWillReviewLoanRequests the whoWillReviewLoanRequests to set
	 */
	public void setWhoWillReviewLoanRequests(String whoWillReviewLoanRequests) {
		this.whoWillReviewLoanRequests = whoWillReviewLoanRequests;
	}

	/**
	 * @return the creatorMayApproveLoanRequestsInd
	 */
	public String getCreatorMayApproveLoanRequestsInd() {
		return creatorMayApproveLoanRequestsInd;
	}

	/**
	 * @param creatorMayApproveLoanRequestsInd the creatorMayApproveLoanRequestsInd to set
	 */
	public void setCreatorMayApproveLoanRequestsInd(
			String creatorMayApproveLoanRequestsInd) {
		this.creatorMayApproveLoanRequestsInd = creatorMayApproveLoanRequestsInd;
	}

	/**
	 * @return the loansChecksMailedTo
	 */
	public String getLoansChecksMailedTo() {
		return loansChecksMailedTo;
	}

	/**
	 * @param loansChecksMailedTo the loansChecksMailedTo to set
	 */
	public void setLoansChecksMailedTo(String loansChecksMailedTo) {
		this.loansChecksMailedTo = loansChecksMailedTo;
	}

	/**
	 * @return the allowLoansPackageToGenerate
	 */
	public String getAllowLoansPackageToGenerate() {
		return allowLoansPackageToGenerate;
	}

	/**
	 * @param allowLoansPackageToGenerate the allowLoansPackageToGenerate to set
	 */
	public void setAllowLoansPackageToGenerate(String allowLoansPackageToGenerate) {
		this.allowLoansPackageToGenerate = allowLoansPackageToGenerate;
	}

	/**
	 * @return the loanRecordKeepingInd
	 */
	public String getLoanRecordKeepingInd() {
		return loanRecordKeepingInd;
	}

	/**
	 * @param loanRecordKeepingInd the loanRecordKeepingInd to set
	 */
	public void setLoanRecordKeepingInd(String loanRecordKeepingInd) {
		this.loanRecordKeepingInd = loanRecordKeepingInd;
	}

	/**
	 * @return the outstandingOldILoanRequestCount
	 */
	public int getOutstandingOldILoanRequestCount() {
		return outstandingOldILoanRequestCount;
	}

	/**
	 * @param outstandingOldILoanRequestCount the outstandingOldILoanRequestCount to set
	 */
	public void setOutstandingOldILoanRequestCount(
			int outstandingOldILoanRequestCount) {
		this.outstandingOldILoanRequestCount = outstandingOldILoanRequestCount;
	}

	public String getConsentInd() {
		return consentInd;
	}

	public void setConsentInd(String consentInd) {
		this.consentInd = consentInd;
	}

	
	/**
	 * @return the isHideConsent
	 */
	public boolean getIsHideConsent() {
		return isHideConsent;
	}

	/**
	 * @param isHideConsent the isHideConsent to set
	 */
	public void setHideConsent(boolean isHideConsent) {
		this.isHideConsent = isHideConsent;
	}

	/**
	 * @return the tpaFirmExists
	 */
	public boolean isTpaFirmExists() {
		return tpaFirmExists;
	}

	/**
	 * @param tpaFirmExists the tpaFirmExists to set
	 */
	public void setTpaFirmExists(boolean tpaFirmExists) {
		this.tpaFirmExists = tpaFirmExists;
	}

	public void setDirectMailInd(String directMailInd) {
		this.directMailInd = directMailInd;
	}

	public String getDirectMailInd() {
		return directMailInd;
	}

    public String getSpecialTaxNotice() {
        return specialTaxNotice;
    }

    public void setSpecialTaxNotice(String specialTaxNotice) {
        this.specialTaxNotice = specialTaxNotice;
    }

	public String getEligibilityCalculationInd() {
		return eligibilityCalculationInd;
	}

	public void setEligibilityCalculationInd(String eligibilityCalculationInd) {
		this.eligibilityCalculationInd = eligibilityCalculationInd;
	}

	public ArrayList<EligibilityCalculationMoneyType> getEligibilityServiceMoneyTypes() {
		return eligibilityServiceMoneyTypes;
	}

	public void setEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes) {
		this.eligibilityServiceMoneyTypes = eligibilityServiceMoneyTypes;
	}

	public ArrayList<EligibilityCalculationMoneyType> getSelectedEligibilityServiceMoneyTypes() {
		return selectedEligibilityServiceMoneyTypes;
	}

	public void setSelectedEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> selectedEligibilityServiceMoneyTypes) {
		this.selectedEligibilityServiceMoneyTypes = selectedEligibilityServiceMoneyTypes;
	}

	public ArrayList<EligibilityCalculationMoneyType> getContractMoneyTypes() {
		return contractMoneyTypes;
	}

	public void setContractMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> contractMoneyTypes) {
		this.contractMoneyTypes = contractMoneyTypes;
	}
	
	public List<MoneyTypeVO> getContractMoneyTypesList() {
		return contractMoneyTypesList;
	}

	public void setContractMoneyTypesList(
			List<MoneyTypeVO> contractMoneyTypesList) {
		this.contractMoneyTypesList = contractMoneyTypesList;
	}

	
	/**
	 * Struts uses this method.
	 */
	public void setEligibityServiceMoneyTypeId(
			EligibilityCalculationMoneyType ecMoneyType) {

		this.eligibilityServiceMoneyTypes.add(ecMoneyType);

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

	/**
	 * 
	 * @return
	 */
	public ArrayList<EligibilityCalculationMoneyType> getLastUpdatedEligibilityServiceMoneyTypes() {
		return lastUpdatedEligibilityServiceMoneyTypes;
	}

	/**
	 * 
	 * @param lastUpdatedEligibilityServiceMoneyTypes
	 */
	public void setLastUpdatedEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypes) {
		this.lastUpdatedEligibilityServiceMoneyTypes = lastUpdatedEligibilityServiceMoneyTypes;
	}

	public boolean isShowEligibilitySection() {
		return showEligibilitySection;
	}

	public void setShowEligibilitySection(boolean showEligibilitySection) {
		this.showEligibilitySection = showEligibilitySection;
	}

	public int getEligibilityServiceMoneyTypesListSize() {
		return eligibilityServiceMoneyTypesListSize;
	}

	public void setEligibilityServiceMoneyTypesListSize(
			int eligibilityServiceMoneyTypesListSize) {
		this.eligibilityServiceMoneyTypesListSize = eligibilityServiceMoneyTypesListSize;
	}

	public String getSummaryPlanHighlightAvailable() {
		return summaryPlanHighlightAvailable;
	}

	public void setSummaryPlanHighlightAvailable(
			String summaryPlanHighlightAvailable) {
		this.summaryPlanHighlightAvailable = summaryPlanHighlightAvailable;
	}
	
	public String getSummaryPlanHighlightReviewed() {
		return summaryPlanHighlightReviewed;
	}

	public void setSummaryPlanHighlightReviewed(
			String summaryPlanHighlightReviewed) {
		this.summaryPlanHighlightReviewed = summaryPlanHighlightReviewed;
	}
	
	@Override
	public void reset( HttpServletRequest request) {
		super.reset( request);

//		this.selectedMoneyTypes = new String[CsfConstants.DEFAULT_EC_MONEY_TYPES_SIZE];
	}	
	
	/**
	 * @return the participantServicesData
	 */
	public ParticipantServicesData getParticipantServicesData() {
		return participantServicesData;
	}

	/**
	 * @param participantServicesData the participantServicesData to set
	 */
	public void setParticipantServicesData(
			ParticipantServicesData participantServicesData) {
		this.participantServicesData = participantServicesData;
	}

	/**
	 * @return the planSponsorServicesData
	 */
	public PlanSponsorServicesData getPlanSponsorServicesData() {
		return planSponsorServicesData;
	}

	/**
	 * @param planSponsorServicesData the planSponsorServicesData to set
	 */
	public void setPlanSponsorServicesData(
			PlanSponsorServicesData planSponsorServicesData) {
		this.planSponsorServicesData = planSponsorServicesData;
	}

	/**
	 * @return the deferralMaxLimitPercent
	 */
	public String getDeferralMaxLimitPercent() {
		return deferralMaxLimitPercent;
	}

	/**
	 * @param deferralMaxLimitPercent the deferralMaxLimitPercent to set
	 */
	public void setDeferralMaxLimitPercent(String deferralMaxLimitPercent) {
		this.deferralMaxLimitPercent = deferralMaxLimitPercent;
	}

	/**
	 * @return the deferralMaxLimitDollars
	 */
	public String getDeferralMaxLimitDollars() {
		return deferralMaxLimitDollars;
	}

	/**
	 * @param deferralMaxLimitDollars the deferralMaxLimitDollars to set
	 */
	public void setDeferralMaxLimitDollars(String deferralMaxLimitDollars) {
		this.deferralMaxLimitDollars = deferralMaxLimitDollars;
	}

	/**
	 * @return the payrollCutoff
	 */
	public String getPayrollCutoff() {
		return payrollCutoff;
	}

	/**
	 * @param payrollCutoff the payrollCutoff to set
	 */
	public void setPayrollCutoff(String payrollCutoff) {
		this.payrollCutoff = payrollCutoff;
	}

	/**
	 * @return the particiapntIATs
	 */
	public String getParticiapntIATs() {
		return particiapntIATs;
	}

	/**
	 * @param particiapntIATs the particiapntIATs to set
	 */
	public void setParticiapntIATs(String particiapntIATs) {
		this.particiapntIATs = particiapntIATs;
	}


    public List<MoneyTypeEligibilityCriterion> getMoneyTypeEligibilityCriteria() {
		return moneyTypeEligibilityCriteria;
	}

	public void setMoneyTypeEligibilityCriteria(
			List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria) {
		this.moneyTypeEligibilityCriteria = moneyTypeEligibilityCriteria;
	}
	
	public Map<String, Date> getMoneyTypesWithFutureDatedDeletionDate() {
		return moneyTypesWithFutureDatedDeletionDate;
	}

	public void setMoneyTypesWithFutureDatedDeletionDate(
			Map<String, Date> moneyTypesWithFutureDatedDeletionDate) {
		this.moneyTypesWithFutureDatedDeletionDate = moneyTypesWithFutureDatedDeletionDate;
	}

	/**
	 * @return the planAciInd
	 */
	public String getPlanAciInd() {
		return planAciInd;
	}

	/**
	 * @param planAciInd the planAciInd to set
	 */
	public void setPlanAciInd(String planAciInd) {
		this.planAciInd = planAciInd;
	}

	/**
	 * @return the planDeferralLimitPercent
	 */
	public String getPlanDeferralLimitPercent() {
		return planDeferralLimitPercent;
	}

	/**
	 * @param planDeferralLimitPercent the planDeferralLimitPercent to set
	 */
	public void setPlanDeferralLimitPercent(String planDeferralLimitPercent) {
		this.planDeferralLimitPercent = planDeferralLimitPercent;
	}

	/**
	 * @return the planDeferralMaxLimitPercent
	 */
	public String getPlanDeferralMaxLimitPercent() {
		return planDeferralMaxLimitPercent;
	}

	/**
	 * @param planDeferralMaxLimitPercent the planDeferralMaxLimitPercent to set
	 */
	public void setPlanDeferralMaxLimitPercent(String planDeferralMaxLimitPercent) {
		this.planDeferralMaxLimitPercent = planDeferralMaxLimitPercent;
	}


	/**
	 * Returns the checkEligibilityCalculationInd.
	 * 
	 * @return checkEligibilityCalculationInd
	 */
	public String getCheckEligibilityCalculationInd() {
		return checkEligibilityCalculationInd;
	}

	/**
	 * Set the checkEligibilityCalculationInd.
	 *  
	 * @param checkEligibilityCalculationInd
	 */
	public void setCheckEligibilityCalculationInd(
			String checkEligibilityCalculationInd) {
		this.checkEligibilityCalculationInd = checkEligibilityCalculationInd;
	}


	public String getResetDeferralValues() {
		return resetDeferralValues;
	}

	public void setResetDeferralValues(String resetDeferralValues) {
		this.resetDeferralValues = resetDeferralValues;
	}

	/**
	 * This method returns the onlineBeneficiaryInd value which will be displayed in CSF page.
	 * 
	 * @return onlineBeneficiaryInd
	 */
	public String getOnlineBeneficiaryInd() {
		return onlineBeneficiaryInd;
	}

	/**
	 * To set the onlineBeneficiaryInd value.
	 * 
	 * @param onlineBeneficiaryInd
	 */
	public void setOnlineBeneficiaryInd(String onlineBeneficiaryInd) {
		this.onlineBeneficiaryInd = onlineBeneficiaryInd;
	}

	public boolean isIpsServiceSuppressed() {
		return ipsServiceSuppressed;
	}

	public void setIpsServiceSuppressed(boolean ipsServiceSuppressed) {
		this.ipsServiceSuppressed = ipsServiceSuppressed;
	}

	/**
	 * @return the coFiduciary
	 */
	public boolean isCoFiduciary() {
		return coFiduciary;
	}

	/**
	 * @param coFiduciary the coFiduciary to set
	 */
	public void setCoFiduciary(boolean coFiduciary) {
		this.coFiduciary = coFiduciary;
	}

	/**
	 * @return the coFidFeatureSuppressed
	 */
	public boolean isCoFidFeatureSuppressed() {
		return coFidFeatureSuppressed;
	}

	/**
	 * @param coFidFeatureSuppressed the coFidFeatureSuppressed to set
	 */
	public void setCoFidFeatureSuppressed(boolean coFidFeatureSuppressed) {
		this.coFidFeatureSuppressed = coFidFeatureSuppressed;
	}

	/**
	 * @return the selectedInvestmentProfile
	 */
	public String getSelectedInvestmentProfile() {
		return selectedInvestmentProfile;
	}

	/**
	 * @param selectedInvestmentProfile the selectedInvestmentProfile to set
	 */
	public void setSelectedInvestmentProfile(String selectedInvestmentProfile) {
		this.selectedInvestmentProfile = selectedInvestmentProfile;
	}

	/**
	 * @return the noticeServiceInd
	 */
	public String getNoticeServiceInd() {
		return noticeServiceInd;
	}

	/**
	 * @param noticeServiceInd the noticeServiceInd to set
	 */
	public void setNoticeServiceInd(String noticeServiceInd) {
		this.noticeServiceInd = noticeServiceInd;
	}

	/**
	 * @return the noticeTypeSelected
	 */
	public String getNoticeTypeSelected() {
		return noticeTypeSelected;
	}

	/**
	 * @param noticeTypeSelected the noticeTypeSelected to set
	 */
	public void setNoticeTypeSelected(String noticeTypeSelected) {
		this.noticeTypeSelected = noticeTypeSelected;
	}

    public boolean isDisplayNoticeGeneration() {
        return displayNoticeGeneration;
    }

    public void setDisplayNoticeGeneration(boolean displayNoticeGeneration) {
        this.displayNoticeGeneration = displayNoticeGeneration;
    }

	public String getPayrollFeedbackService() {
		return payrollFeedbackService;
	}

	public void setPayrollFeedbackService(String payrollFeedbackService) {
		this.payrollFeedbackService = payrollFeedbackService;
	}
	
	public boolean isPlanEligibleForManagedAccounts() {
		return planEligibleForManagedAccounts;
	}

	public void setPlanEligibleForManagedAccounts(boolean planEligibleForManagedAccounts) {
		this.planEligibleForManagedAccounts = planEligibleForManagedAccounts;
	}

	public ManagedAccountServiceFeatureLite getManagedAccountServiceFeature() {
		return managedAccountServiceFeature;
	}

	public void setManagedAccountServiceFeature(ManagedAccountServiceFeatureLite managedAccountServiceFeature) {
		this.managedAccountServiceFeature = managedAccountServiceFeature;
	}

	public boolean isManagedAccountSectionEditable() {
		return managedAccountSectionEditable;
	}

	public void setManagedAccountSectionEditable(boolean managedAccountSectionEditable) {
		this.managedAccountSectionEditable = managedAccountSectionEditable;
	}

	public String getManagedAccountServiceAvailableToPptDate() {
		return managedAccountServiceAvailableToPptDate;
	}

	public void setManagedAccountServiceAvailableToPptDate(String managedAccountServiceAvailableToPptDate) {
		this.managedAccountServiceAvailableToPptDate = managedAccountServiceAvailableToPptDate;
	}

	public boolean isShowNoticesEdeliveryInViewPage() {
		return showNoticesEdeliveryInViewPage;
	}

	public void setShowNoticesEdeliveryInViewPage(boolean showNoticesEdeliveryInViewPage) {
		this.showNoticesEdeliveryInViewPage = showNoticesEdeliveryInViewPage;
	}
	
	
}
