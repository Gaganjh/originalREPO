/**
 * 
 */
package com.manulife.pension.ps.web.contract.csf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * CMA keys for the PlanSponsor Services section
 *  
 * @author Puttaiah Arugunta
 *
 */
public class PlanSponsorServicesData  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int summaryPlanHighlightAvailable;
	private int summaryPlanHighlightReviewed;
	
	private boolean noticeGenerationServiceInd;
	private String noticeOption;
	private String noticeServiceSelectedDate;
    private String noticeServiceDeSelectedDate;
	
	private int eligibilityCalculationService;
	private boolean isECon;
	
	private int jhEZstart;
	private boolean isEzStartOn;
	private ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes = 
		new ArrayList<EligibilityCalculationMoneyType>();
	private int initialEnrollmentDate ;
	private String initialEnrollmentParam;
	private int directMailEnrollment ;
	
	private int jhEZincrease;
	private boolean isEzIncreaseOn;
	private int firstScheduledIncrease;
	private String firstSchedParam;
	private boolean isEzIncreaseCustomized;
	private int initialIncreaseAnniversaryDate;
	
	private int vesting;
	private boolean isVestingOn;
	private boolean isVestingLabelSurpressed;
	private int reportVestingPercentages ;
	private int payrollFrequency;
	private String payrollFreqParam;
	private int allowPayrollPathSubmissions;
	private boolean isConsentallowed;
	private int consentSubsequentAmendments;
	
	private int onlineLoans;
	private boolean isOnlineLoansAllowed;
	private int loanChecksMailed; 
	private String loanMailedParam;
	private int loanApprover;
	private boolean isLoanLabelSurpressed;
	private int loanPackagesGenerated;
	private int loanPriorApproval;
	
	private int iWithdrawals;
	private boolean isIWithdrawalsAllowed;
	private int withdrawalIRSSpecialTaxNotices;
	private int withdrawalChecksMailed; 
	private String withdrawMailedParam;
	private boolean isWithdrawalLabelSurpressed;
	private int withdrawalPriorApproval;
	private int withdrawalApproval;
	
	// aka Investment Policy Statement
	private int ipsServiceContent;
	private int annualReviewProcessingDateContent;
	private String annualReviewProcessingDateContentParam;
	private String ipsService;
	
	private int payrollFeedbackServiceContent;
	
    
	/**
	 * @return the summaryPlanHighlightReviewed
	 */
	public int getSummaryPlanHighlightReviewed() {
		return summaryPlanHighlightReviewed;
	}
	/**
	 * @param summaryPlanHighlightReviewed the summaryPlanHighlightReviewed to set
	 */
	public void setSummaryPlanHighlightReviewed(int summaryPlanHighlightReviewed) {
		this.summaryPlanHighlightReviewed = summaryPlanHighlightReviewed;
	}
	
	/**
	 * @return the summaryPlanHighlightAvailable
	 */
	public int getSummaryPlanHighlightAvailable() {
		return summaryPlanHighlightAvailable;
	}
	/**
	 * @param summaryPlanHighlightAvailable the summaryPlanHighlightAvailable to set
	 */
	public void setSummaryPlanHighlightAvailable(int summaryPlanHighlightAvailable) {
		this.summaryPlanHighlightAvailable = summaryPlanHighlightAvailable;
	}
	
	public boolean isNoticeGenerationServiceInd() {
        return noticeGenerationServiceInd;
    }
    public void setNoticeGenerationServiceInd(boolean noticeGenerationServiceInd) {
        this.noticeGenerationServiceInd = noticeGenerationServiceInd;
    }
    
    public String getNoticeOption() {
        return noticeOption;
    }
    public void setNoticeOption(String noticeOption) {
        this.noticeOption = noticeOption;
    }
    public String getNoticeServiceSelectedDate() {
		return noticeServiceSelectedDate;
	}
	public void setNoticeServiceSelectedDate(String noticeServiceSelectedDate) {
		this.noticeServiceSelectedDate = noticeServiceSelectedDate;
	}
	public String getNoticeServiceDeSelectedDate() {
		return noticeServiceDeSelectedDate;
	}
	public void setNoticeServiceDeSelectedDate(String noticeServiceDeSelectedDate) {
		this.noticeServiceDeSelectedDate = noticeServiceDeSelectedDate;
	}
	/**
	 * @return the eligibilityCalculationService
	 */
	public int getEligibilityCalculationService() {
		return eligibilityCalculationService;
	}
	/**
	 * @param eligibilityCalculationService the eligibilityCalculationService to set
	 */
	public void setEligibilityCalculationService(int eligibilityCalculationService) {
		this.eligibilityCalculationService = eligibilityCalculationService;
	}
	
	/**
	 * @return the isECon
	 */
	public boolean getIsECon() {
		return isECon;
	}
	/**
	 * @param isECon the isECon to set
	 */
	public void setECon(boolean isECon) {
		this.isECon = isECon;
	}
	
	/**
	 * @return the eligibilityServiceMoneyTypes
	 */
	public ArrayList<EligibilityCalculationMoneyType> getEligibilityServiceMoneyTypes() {
		return eligibilityServiceMoneyTypes;
	}
	/**
	 * @param eligibilityServiceMoneyTypes the eligibilityServiceMoneyTypes to set
	 */
	public void setEligibilityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes) {
		this.eligibilityServiceMoneyTypes = eligibilityServiceMoneyTypes;
	}
	/**
	 * @return the jhEZstart
	 */
	public int getJhEZstart() {
		return jhEZstart;
	}
	/**
	 * @param jhEZstart the jhEZstart to set
	 */
	public void setJhEZstart(int jhEZstart) {
		this.jhEZstart = jhEZstart;
	}
	/**
	 * @return the isEzStartOn
	 */
	public boolean getIsEzStartOn() {
		return isEzStartOn;
	}
	/**
	 * @param isEzStartOn the isEzStartOn to set
	 */
	public void setEzStartOn(boolean isEzStartOn) {
		this.isEzStartOn = isEzStartOn;
	}
	/**
	 * @return the initialEnrollmentDate
	 */
	public int getInitialEnrollmentDate() {
		return initialEnrollmentDate;
	}
	/**
	 * @param initialEnrollmentDate the initialEnrollmentDate to set
	 */
	public void setInitialEnrollmentDate(int initialEnrollmentDate) {
		this.initialEnrollmentDate = initialEnrollmentDate;
	}
	
	/**
	 * @return the initialEnrollmentParam
	 */
	public String getInitialEnrollmentParam() {
		return initialEnrollmentParam;
	}
	/**
	 * @param initialEnrollmentParam the initialEnrollmentParam to set
	 */
	public void setInitialEnrollmentParam(String initialEnrollmentParam) {
		this.initialEnrollmentParam = initialEnrollmentParam;
	}
	/**
	 * @return the directMailEnrollment
	 */
	public int getDirectMailEnrollment() {
		return directMailEnrollment;
	}
	/**
	 * @param directMailEnrollment the directMailEnrollment to set
	 */
	public void setDirectMailEnrollment(int directMailEnrollment) {
		this.directMailEnrollment = directMailEnrollment;
	}
	/**
	 * @return the jhEZincrease
	 */
	public int getJhEZincrease() {
		return jhEZincrease;
	}
	/**
	 * @param jhEZincrease the jhEZincrease to set
	 */
	public void setJhEZincrease(int jhEZincrease) {
		this.jhEZincrease = jhEZincrease;
	}
	/**
	 * @return the isEzIncreaseOn
	 */
	public boolean getIsEzIncreaseOn() {
		return isEzIncreaseOn;
	}
	/**
	 * @param isEzIncreaseOn the isEzIncreaseOn to set
	 */
	public void setEzIncreaseOn(boolean isEzIncreaseOn) {
		this.isEzIncreaseOn = isEzIncreaseOn;
	}
	/**
	 * @return the firstScheduledIncrease
	 */
	public int getFirstScheduledIncrease() {
		return firstScheduledIncrease;
	}
	/**
	 * @param firstScheduledIncrease the firstScheduledIncrease to set
	 */
	public void setFirstScheduledIncrease(int firstScheduledIncrease) {
		this.firstScheduledIncrease = firstScheduledIncrease;
	}
	/**
	 * @return the firstSchedParam
	 */
	public String getFirstSchedParam() {
		return firstSchedParam;
	}
	/**
	 * @param firstSchedParam the firstSchedParam to set
	 */
	public void setFirstSchedParam(String firstSchedParam) {
		this.firstSchedParam = firstSchedParam;
	}
	/**
	 * @return the isEzIncreaseCustomized
	 */
	public boolean getIsEzIncreaseCustomized() {
		return isEzIncreaseCustomized;
	}
	/**
	 * @param isEzIncreaseCustomized the isEzIncreaseCustomized to set
	 */
	public void setEzIncreaseCustomized(boolean isEzIncreaseCustomized) {
		this.isEzIncreaseCustomized = isEzIncreaseCustomized;
	}
	/**
	 * @return the initialIncreaseAnniversaryDate
	 */
	public int getInitialIncreaseAnniversaryDate() {
		return initialIncreaseAnniversaryDate;
	}
	/**
	 * @param initialIncreaseAnniversaryDate the initialIncreaseAnniversaryDate to set
	 */
	public void setInitialIncreaseAnniversaryDate(int initialIncreaseAnniversaryDate) {
		this.initialIncreaseAnniversaryDate = initialIncreaseAnniversaryDate;
	}
	/**
	 * @return the vesting
	 */
	public int getVesting() {
		return vesting;
	}
	/**
	 * @param vesting the vesting to set
	 */
	public void setVesting(int vesting) {
		this.vesting = vesting;
	}
	/**
	 * @return the isVestingOn
	 */
	public boolean getIsVestingOn() {
		return isVestingOn;
	}
	/**
	 * @param isVestingOn the isVestingOn to set
	 */
	public void setVestingOn(boolean isVestingOn) {
		this.isVestingOn = isVestingOn;
	}
	/**
	 * @return the isVestingLabelSurpressed
	 */
	public boolean getIsVestingLabelSurpressed() {
		return isVestingLabelSurpressed;
	}
	/**
	 * @param isVestingLabelSurpressed the isVestingLabelSurpressed to set
	 */
	public void setVestingLabelSurpressed(boolean isVestingLabelSurpressed) {
		this.isVestingLabelSurpressed = isVestingLabelSurpressed;
	}
	/**
	 * @return the reportVestingPercentages
	 */
	public int getReportVestingPercentages() {
		return reportVestingPercentages;
	}
	/**
	 * @param reportVestingPercentages the reportVestingPercentages to set
	 */
	public void setReportVestingPercentages(int reportVestingPercentages) {
		this.reportVestingPercentages = reportVestingPercentages;
	}
	/**
	 * @return the payrollFrequency
	 */
	public int getPayrollFrequency() {
		return payrollFrequency;
	}
	/**
	 * @param payrollFrequency the payrollFrequency to set
	 */
	public void setPayrollFrequency(int payrollFrequency) {
		this.payrollFrequency = payrollFrequency;
	}
	/**
	 * @return the payrollFreqParam
	 */
	public String getPayrollFreqParam() {
		return payrollFreqParam;
	}
	/**
	 * @param payrollFreqParam the payrollFreqParam to set
	 */
	public void setPayrollFreqParam(String payrollFreqParam) {
		this.payrollFreqParam = payrollFreqParam;
	}
	/**
	 * @return the allowPayrollPathSubmissions
	 */
	public int getAllowPayrollPathSubmissions() {
		return allowPayrollPathSubmissions;
	}
	/**
	 * @param allowPayrollPathSubmissions the allowPayrollPathSubmissions to set
	 */
	public void setAllowPayrollPathSubmissions(int allowPayrollPathSubmissions) {
		this.allowPayrollPathSubmissions = allowPayrollPathSubmissions;
	}
	
	/**
	 * @return the isConsentallowed
	 */
	public boolean getIsConsentallowed() {
		return isConsentallowed;
	}
	/**
	 * @param isConsentallowed the isConsentallowed to set
	 */
	public void setConsentallowed(boolean isConsentallowed) {
		this.isConsentallowed = isConsentallowed;
	}
	/**
	 * @return the consentSubsequentAmendments
	 */
	public int getConsentSubsequentAmendments() {
		return consentSubsequentAmendments;
	}
	/**
	 * @param consentSubsequentAmendments the consentSubsequentAmendments to set
	 */
	public void setConsentSubsequentAmendments(int consentSubsequentAmendments) {
		this.consentSubsequentAmendments = consentSubsequentAmendments;
	}
	/**
	 * @return the onlineLoans
	 */
	public int getOnlineLoans() {
		return onlineLoans;
	}
	/**
	 * @param onlineLoans the onlineLoans to set
	 */
	public void setOnlineLoans(int onlineLoans) {
		this.onlineLoans = onlineLoans;
	}
	/**
	 * @return the isOnlineLoansAllowed
	 */
	public boolean getIsOnlineLoansAllowed() {
		return isOnlineLoansAllowed;
	}
	/**
	 * @param isOnlineLoansAllowed the isOnlineLoansAllowed to set
	 */
	public void setOnlineLoansAllowed(boolean isOnlineLoansAllowed) {
		this.isOnlineLoansAllowed = isOnlineLoansAllowed;
	}
	/**
	 * @return the loanChecksMailed
	 */
	public int getLoanChecksMailed() {
		return loanChecksMailed;
	}
	/**
	 * @param loanChecksMailed the loanChecksMailed to set
	 */
	public void setLoanChecksMailed(int loanChecksMailed) {
		this.loanChecksMailed = loanChecksMailed;
	}
	
	/**
	 * @return the loanMailedParam
	 */
	public String getLoanMailedParam() {
		return loanMailedParam;
	}
	/**
	 * @param loanMailedParam the loanMailedParam to set
	 */
	public void setLoanMailedParam(String loanMailedParam) {
		this.loanMailedParam = loanMailedParam;
	}
	/**
	 * @return the loanApprover
	 */
	public int getLoanApprover() {
		return loanApprover;
	}
	/**
	 * @param loanApprover the loanApprover to set
	 */
	public void setLoanApprover(int loanApprover) {
		this.loanApprover = loanApprover;
	}
	/**
	 * @return the isLoanLabelSurpressed
	 */
	public boolean getIsLoanLabelSurpressed() {
		return isLoanLabelSurpressed;
	}
	/**
	 * @param isLoanLabelSurpressed the isLoanLabelSurpressed to set
	 */
	public void setLoanLabelSurpressed(boolean isLoanLabelSurpressed) {
		this.isLoanLabelSurpressed = isLoanLabelSurpressed;
	}
	/**
	 * @return the loanPackagesGenerated
	 */
	public int getLoanPackagesGenerated() {
		return loanPackagesGenerated;
	}
	/**
	 * @param loanPackagesGenerated the loanPackagesGenerated to set
	 */
	public void setLoanPackagesGenerated(int loanPackagesGenerated) {
		this.loanPackagesGenerated = loanPackagesGenerated;
	}
	/**
	 * @return the loanPriorApproval
	 */
	public int getLoanPriorApproval() {
		return loanPriorApproval;
	}
	/**
	 * @param loanPriorApproval the loanPriorApproval to set
	 */
	public void setLoanPriorApproval(int loanPriorApproval) {
		this.loanPriorApproval = loanPriorApproval;
	}
	/**
	 * @return the iWithdrawals
	 */
	public int getiWithdrawals() {
		return iWithdrawals;
	}
	/**
	 * @param iWithdrawals the iWithdrawals to set
	 */
	public void setiWithdrawals(int iWithdrawals) {
		this.iWithdrawals = iWithdrawals;
	}
	/**
	 * @return the isIWithdrawalsAllowed
	 */
	public boolean getIsIWithdrawalsAllowed() {
		return isIWithdrawalsAllowed;
	}
	/**
	 * @param isIWithdrawalsAllowed the isIWithdrawalsAllowed to set
	 */
	public void setIWithdrawalsAllowed(boolean isIWithdrawalsAllowed) {
		this.isIWithdrawalsAllowed = isIWithdrawalsAllowed;
	}
	/**
	 * @return the withdrawalIRSSpecialTaxNotices
	 */
	public int getWithdrawalIRSSpecialTaxNotices() {
		return withdrawalIRSSpecialTaxNotices;
	}
	/**
	 * @param withdrawalIRSSpecialTaxNotices the withdrawalIRSSpecialTaxNotices to set
	 */
	public void setWithdrawalIRSSpecialTaxNotices(int withdrawalIRSSpecialTaxNotices) {
		this.withdrawalIRSSpecialTaxNotices = withdrawalIRSSpecialTaxNotices;
	}
	/**
	 * @return the withdrawalChecksMailed
	 */
	public int getWithdrawalChecksMailed() {
		return withdrawalChecksMailed;
	}
	/**
	 * @param withdrawalChecksMailed the withdrawalChecksMailed to set
	 */
	public void setWithdrawalChecksMailed(int withdrawalChecksMailed) {
		this.withdrawalChecksMailed = withdrawalChecksMailed;
	}
	/**
	 * @return the withdrawMailedParam
	 */
	public String getWithdrawMailedParam() {
		return withdrawMailedParam;
	}
	/**
	 * @param withdrawMailedParam the withdrawMailedParam to set
	 */
	public void setWithdrawMailedParam(String withdrawMailedParam) {
		this.withdrawMailedParam = withdrawMailedParam;
	}
	/**
	 * @return the isWithdrawalLabelSurpressed
	 */
	public boolean getIsWithdrawalLabelSurpressed() {
		return isWithdrawalLabelSurpressed;
	}
	/**
	 * @param isWithdrawalLabelSurpressed the isWithdrawalLabelSurpressed to set
	 */
	public void setWithdrawalLabelSurpressed(boolean isWithdrawalLabelSurpressed) {
		this.isWithdrawalLabelSurpressed = isWithdrawalLabelSurpressed;
	}
	/**
	 * @return the withdrawalPriorApproval
	 */
	public int getWithdrawalPriorApproval() {
		return withdrawalPriorApproval;
	}
	/**
	 * @param withdrawalPriorApproval the withdrawalPriorApproval to set
	 */
	public void setWithdrawalPriorApproval(int withdrawalPriorApproval) {
		this.withdrawalPriorApproval = withdrawalPriorApproval;
	}
	/**
	 * @return the withdrawalApproval
	 */
	public int getWithdrawalApproval() {
		return withdrawalApproval;
	}
	/**
	 * @param withdrawalApproval the withdrawalApproval to set
	 */
	public void setWithdrawalApproval(int withdrawalApproval) {
		this.withdrawalApproval = withdrawalApproval;
	}

    public int getIpsServiceContent() {
        return ipsServiceContent;
    }
    public void setIpsServiceContent(int ipsServiceContent) {
        this.ipsServiceContent = ipsServiceContent;
    }
    public int getAnnualReviewProcessingDateContent() {
        return annualReviewProcessingDateContent;
    }
    public void setAnnualReviewProcessingDateContent(int annualReviewProcessingDateContent) {
        this.annualReviewProcessingDateContent = annualReviewProcessingDateContent;
    }
    public String getAnnualReviewProcessingDateContentParam() {
        return annualReviewProcessingDateContentParam;
    }
    public void setAnnualReviewProcessingDateContentParam(String annualReviewProcessingDateContentParam) {
        this.annualReviewProcessingDateContentParam = annualReviewProcessingDateContentParam;
    }
    public String getIpsService() {
        return ipsService;
    }
    public void setIpsService(String ipsService) {
        this.ipsService = ipsService;
    }
	public int getPayrollFeedbackServiceContent() {
		return payrollFeedbackServiceContent;
	}
	public void setPayrollFeedbackServiceContent(int payrollFeedbackServiceContent) {
		this.payrollFeedbackServiceContent = payrollFeedbackServiceContent;
	}

}