/**
 * 
 */
package com.manulife.pension.ps.web.contract.csf;

import java.io.Serializable;
import java.util.List;

/**
 * CMA keys for the PlanSponsor Services section
 * 
 * @author Puttaiah Arugunta
 */
public class ParticipantServicesData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//Payroll Support Services sub-section
	private int participantOnlineAddressChanges;
	private String addressParam;
	private String moreThanOneOptionForAddress;
	private int participantDeferrelAmtType;
	private int participantDeferrelsOnline1;
	private int participantDeferrelsOnline2;
	private int participantsAreAllowedToEnrolledOnline;
	// Our standard service 
	private boolean determineOurStandardServiceEligible ;
	private int defaultDeferralScheduledIncreaseAmtAndMax;
	private List<String> deferrelSchedParamas;
	private int payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges;
	private String payrollParam;
	
	//Financial Transactions sub-section
	private int participantsCanInitiateInterAccountTransfersOnline;
	private int participantsCanInitiateOnlineLoanRequests;
	private boolean isLoansAllowed;
	private boolean isJHdoesLoanRK;
	private int participantsCanInitiateWithdrawalRequests;
	
	//Beneficiary Information sub-section
	private boolean onlineBeneficiaryDesignationAllowed;
	
	/**
	 * @return the participantOnlineAddressChanges
	 */
	public int getParticipantOnlineAddressChanges() {
		return participantOnlineAddressChanges;
	}
	/**
	 * @param participantOnlineAddressChanges the participantOnlineAddressChanges to set
	 */
	public void setParticipantOnlineAddressChanges(
			int participantOnlineAddressChanges) {
		this.participantOnlineAddressChanges = participantOnlineAddressChanges;
	}
	
	/**
	 * @return the addressParam
	 */
	public String getAddressParam() {
		return addressParam;
	}
	/**
	 * @param addressParam the addressParam to set
	 */
	public void setAddressParam(String addressParam) {
		this.addressParam = addressParam;
	}
	/**
	 * @return the participantDeferrelAmtType
	 */
	public int getParticipantDeferrelAmtType() {
		return participantDeferrelAmtType;
	}
	/**
	 * @param participantDeferrelAmtType the participantDeferrelAmtType to set
	 */
	public void setParticipantDeferrelAmtType(int participantDeferrelAmtType) {
		this.participantDeferrelAmtType = participantDeferrelAmtType;
	}
	/**
	 * @return the participantDeferrelsOnline1
	 */
	public int getParticipantDeferrelsOnline1() {
		return participantDeferrelsOnline1;
	}
	/**
	 * @param participantDeferrelsOnline1 the participantDeferrelsOnline1 to set
	 */
	public void setParticipantDeferrelsOnline1(int participantDeferrelsOnline1) {
		this.participantDeferrelsOnline1 = participantDeferrelsOnline1;
	}
	/**
	 * @return the participantDeferrelsOnline2
	 */
	public int getParticipantDeferrelsOnline2() {
		return participantDeferrelsOnline2;
	}
	/**
	 * @param participantDeferrelsOnline2 the participantDeferrelsOnline2 to set
	 */
	public void setParticipantDeferrelsOnline2(int participantDeferrelsOnline2) {
		this.participantDeferrelsOnline2 = participantDeferrelsOnline2;
	}
	/**
	 * @return the participantsAreAllowedToEnrolledOnline
	 */
	public int getParticipantsAreAllowedToEnrolledOnline() {
		return participantsAreAllowedToEnrolledOnline;
	}
	/**
	 * @param participantsAreAllowedToEnrolledOnline the participantsAreAllowedToEnrolledOnline to set
	 */
	public void setParticipantsAreAllowedToEnrolledOnline(
			int participantsAreAllowedToEnrolledOnline) {
		this.participantsAreAllowedToEnrolledOnline = participantsAreAllowedToEnrolledOnline;
	}
	/**
	 * @return the determineOurStandardServiceEligible
	 */
	public boolean getDetermineOurStandardServiceEligible() {
		return determineOurStandardServiceEligible;
	}
	/**
	 * @param determineOurStandardServiceEligible the determineOurStandardServiceEligible to set
	 */
	public void setDetermineOurStandardServiceEligible(
			boolean determineOurStandardServiceEligible) {
		this.determineOurStandardServiceEligible = determineOurStandardServiceEligible;
	}
	/**
	 * @return the defaultDeferralScheduledIncreaseAmtAndMax
	 */
	public int getDefaultDeferralScheduledIncreaseAmtAndMax() {
		return defaultDeferralScheduledIncreaseAmtAndMax;
	}
	/**
	 * @param defaultDeferralScheduledIncreaseAmtAndMax the defaultDeferralScheduledIncreaseAmtAndMax to set
	 */
	public void setDefaultDeferralScheduledIncreaseAmtAndMax(
			int defaultDeferralScheduledIncreaseAmtAndMax) {
		this.defaultDeferralScheduledIncreaseAmtAndMax = defaultDeferralScheduledIncreaseAmtAndMax;
	}
	
	/**
	 * @return the deferrelSchedParamas
	 */
	public List<String> getDeferrelSchedParamas() {
		return deferrelSchedParamas;
	}
	/**
	 * @param deferrelSchedParamas the deferrelSchedParamas to set
	 */
	public void setDeferrelSchedParamas(List<String> deferrelSchedParamas) {
		this.deferrelSchedParamas = deferrelSchedParamas;
	}
	/**
	 * @return the payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges
	 */
	public int getPayrollCutOffForOnlineDeferralAndAutoEnrollmentChanges() {
		return payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges;
	}
	/**
	 * @param payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges the payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges to set
	 */
	public void setPayrollCutOffForOnlineDeferralAndAutoEnrollmentChanges(
			int payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges) {
		this.payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges = payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges;
	}
	
	/**
	 * @return the payrollParam
	 */
	public String getPayrollParam() {
		return payrollParam;
	}
	/**
	 * @param payrollParam the payrollParam to set
	 */
	public void setPayrollParam(String payrollParam) {
		this.payrollParam = payrollParam;
	}
	/**
	 * @return the participantsCanInitiateInterAccountTransfersOnline
	 */
	public int getParticipantsCanInitiateInterAccountTransfersOnline() {
		return participantsCanInitiateInterAccountTransfersOnline;
	}
	/**
	 * @param participantsCanInitiateInterAccountTransfersOnline the participantsCanInitiateInterAccountTransfersOnline to set
	 */
	public void setParticipantsCanInitiateInterAccountTransfersOnline(
			int participantsCanInitiateInterAccountTransfersOnline) {
		this.participantsCanInitiateInterAccountTransfersOnline = participantsCanInitiateInterAccountTransfersOnline;
	}
	/**
	 * @return the participantsCanInitiateOnlineLoanRequests
	 */
	public int getParticipantsCanInitiateOnlineLoanRequests() {
		return participantsCanInitiateOnlineLoanRequests;
	}
	/**
	 * @param participantsCanInitiateOnlineLoanRequests the participantsCanInitiateOnlineLoanRequests to set
	 */
	public void setParticipantsCanInitiateOnlineLoanRequests(
			int participantsCanInitiateOnlineLoanRequests) {
		this.participantsCanInitiateOnlineLoanRequests = participantsCanInitiateOnlineLoanRequests;
	}
	
	/**
	 * @return the isLoansAllowed
	 */
	public boolean getIsLoansAllowed() {
		return isLoansAllowed;
	}
	/**
	 * @param isLoansAllowed the isLoansAllowed to set
	 */
	public void setLoansAllowed(boolean isLoansAllowed) {
		this.isLoansAllowed = isLoansAllowed;
	}
	
	/**
	 * @return the isJHdoesLoanRK
	 */
	public boolean getIsJHdoesLoanRK() {
		return isJHdoesLoanRK;
	}
	/**
	 * @param isJHdoesLoanRK the isJHdoesLoanRK to set
	 */
	public void setJHdoesLoanRK(boolean isJHdoesLoanRK) {
		this.isJHdoesLoanRK = isJHdoesLoanRK;
	}
	/**
	 * @return the participantsCanInitiateWithdrawalRequests
	 */
	public int getParticipantsCanInitiateWithdrawalRequests() {
		return participantsCanInitiateWithdrawalRequests;
	}
	/**
	 * @param participantsCanInitiateWithdrawalRequests the participantsCanInitiateWithdrawalRequests to set
	 */
	public void setParticipantsCanInitiateWithdrawalRequests(
			int participantsCanInitiateWithdrawalRequests) {
		this.participantsCanInitiateWithdrawalRequests = participantsCanInitiateWithdrawalRequests;
	}

	/**
	 * @return the moreThanOneOptionForAddress
	 */
	public String getMoreThanOneOptionForAddress() {
		return moreThanOneOptionForAddress;
	}
	/**
	 * @param moreThanOneOptionForAddress the moreThanOneOptionForAddress to set
	 */
	public void setMoreThanOneOptionForAddress(String moreThanOneOptionForAddress) {
		this.moreThanOneOptionForAddress = moreThanOneOptionForAddress;
	}
	
	/**
	 * 
	 * @return onlineBeneficiaryDesignationAllowed
	 */
	public boolean isOnlineBeneficiaryDesignationAllowed() {
		return onlineBeneficiaryDesignationAllowed;
	}
	
	/**
	 * To set onlineBeneficiaryDesignationAllowed
	 * 
	 * @param onlineBeneficiaryDesignationAllowed
	 */
	public void setOnlineBeneficiaryDesignationAllowed(boolean onlineBeneficiaryDesignation) {
		this.onlineBeneficiaryDesignationAllowed = onlineBeneficiaryDesignation;
	}
	
}
