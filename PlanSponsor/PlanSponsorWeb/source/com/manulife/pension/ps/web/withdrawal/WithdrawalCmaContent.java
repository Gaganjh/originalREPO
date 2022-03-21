package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * Holds all the required CMA content.
 * 
 * Used only for Withdrawals PDF.
 * 
 * @author ayyalsa
 *
 */
public class WithdrawalCmaContent extends BaseSerializableCloneableObject {

	private String activityHistory = StringUtils.EMPTY;
	private String accountBalanceFootnotePbaAndLoan = StringUtils.EMPTY;
	private String accountBalanceFootnotePbaOnly = StringUtils.EMPTY;
	private String accountBalanceFootnoteLoanOnly = StringUtils.EMPTY;

	private String tax1099rSectionTitle = StringUtils.EMPTY;
	private String declarationsSectionTitle = StringUtils.EMPTY;
	private String declarationTaxNoticeText = StringUtils.EMPTY;
	private String declarationTaxNoticeLinkEzk = StringUtils.EMPTY;
	private String declarationTaxNoticeLinkPsw = StringUtils.EMPTY;
	private String waitingPeriodText = StringUtils.EMPTY;
	private String iraProviderText = StringUtils.EMPTY;
	private String legaleseText = StringUtils.EMPTY;
	private String participantLegalese = StringUtils.EMPTY;
	private String riskIndicatorText = StringUtils.EMPTY;
	private String loanInformation = StringUtils.EMPTY;
	private String step1PageBeanBody1 = StringUtils.EMPTY;
	private String step1PageBeanBody2 = StringUtils.EMPTY;
	private String step1PageBeanBody3 = StringUtils.EMPTY;
	private String step2PageBeanBody3Header = StringUtils.EMPTY;
	private String step2PageBeanBody1 = StringUtils.EMPTY;
	private String step2PageBeanBody2 = StringUtils.EMPTY;
	private String step2PageBeanBody3 = StringUtils.EMPTY;
	private String participantInitiatedRequestMessage = StringUtils.EMPTY;
	private String psTPAInitiatedRequestMessage = StringUtils.EMPTY;
	private String maskedAccountNumber = StringUtils.EMPTY;
	private List<String> step2PageBeanBody1HeaderList = new ArrayList<String>();
	private List<String> step2PageBeanBody2HeaderList = new ArrayList<String>();
	
	// NOTES section
	private String notesSectionTitle = StringUtils.EMPTY;
	private String notesSpecialContent = StringUtils.EMPTY;

	// Participant Information Section
	private String personalInformation = StringUtils.EMPTY;
	private String employeeSnapshot = StringUtils.EMPTY;
	
	// Payment Instruction section
	private String paymentInstructionsSectionTitle = StringUtils.EMPTY;
	private String eftPayeeSectionTitle = StringUtils.EMPTY;
	private String chequePayeeSectionTitle = StringUtils.EMPTY;
	private String overrideCsfMailText = StringUtils.EMPTY;
	private String payeexSectionContent = StringUtils.EMPTY;
	
	// withdrawal amount section
	private String withdrawalAmountSectionTitle = StringUtils.EMPTY;
	private String unvestedMoneySectionContent = StringUtils.EMPTY;
	private String vestingInformationText = StringUtils.EMPTY;
	private String taxWithholdingSectionTitle = StringUtils.EMPTY;
	private String participantHasPartialStatusText = StringUtils.EMPTY;
	private String participantHasPre1987MoneyTypeText = StringUtils.EMPTY;
	private String withdrawalReasonIsFullyVestedText = StringUtils.EMPTY;
	
	// Basic information
	private String basicInformation = StringUtils.EMPTY;
	private String lastProcessedDateCommentText = StringUtils.EMPTY;
	
	// Confirmation
	private String confirmationText1 = StringUtils.EMPTY;
	private String confirmationText2 = StringUtils.EMPTY;
	private String confirmationText3 = StringUtils.EMPTY;
	
	private String footer1 = StringUtils.EMPTY;
	private List<String> disclaimer = null;
	private List<String> footnotes = null;
	
	/**
	 * @return the activityHistory
	 */
	public String getActivityHistory() {
		return activityHistory;
	}

	/**
	 * @param activityHistory the activityHistory to set
	 */
	public void setActivityHistory(String activityHistory) {
		this.activityHistory = activityHistory;
	}

	/**
	 * @return the accountBalanceFootnotePbaAndLoan
	 */
	public String getAccountBalanceFootnotePbaAndLoan() {
		return accountBalanceFootnotePbaAndLoan;
	}

	/**
	 * @param accountBalanceFootnotePbaAndLoan the accountBalanceFootnotePbaAndLoan to set
	 */
	public void setAccountBalanceFootnotePbaAndLoan(
			String accountBalanceFootnotePbaAndLoan) {
		this.accountBalanceFootnotePbaAndLoan = accountBalanceFootnotePbaAndLoan;
	}

	/**
	 * @return the accountBalanceFootnotePbaOnly
	 */
	public String getAccountBalanceFootnotePbaOnly() {
		return accountBalanceFootnotePbaOnly;
	}

	/**
	 * @param accountBalanceFootnotePbaOnly the accountBalanceFootnotePbaOnly to set
	 */
	public void setAccountBalanceFootnotePbaOnly(
			String accountBalanceFootnotePbaOnly) {
		this.accountBalanceFootnotePbaOnly = accountBalanceFootnotePbaOnly;
	}

	/**
	 * @return the accountBalanceFootnoteLoanOnly
	 */
	public String getAccountBalanceFootnoteLoanOnly() {
		return accountBalanceFootnoteLoanOnly;
	}

	/**
	 * @param accountBalanceFootnoteLoanOnly the accountBalanceFootnoteLoanOnly to set
	 */
	public void setAccountBalanceFootnoteLoanOnly(
			String accountBalanceFootnoteLoanOnly) {
		this.accountBalanceFootnoteLoanOnly = accountBalanceFootnoteLoanOnly;
	}

	/**
	 * @return the tax1099rSectionTitle
	 */
	public String getTax1099rSectionTitle() {
		return tax1099rSectionTitle;
	}

	/**
	 * @param tax1099rSectionTitle the tax1099rSectionTitle to set
	 */
	public void setTax1099rSectionTitle(String tax1099rSectionTitle) {
		this.tax1099rSectionTitle = tax1099rSectionTitle;
	}

	/**
	 * @return the declarationsSectionTitle
	 */
	public String getDeclarationsSectionTitle() {
		return declarationsSectionTitle;
	}

	/**
	 * @param declarationsSectionTitle the declarationsSectionTitle to set
	 */
	public void setDeclarationsSectionTitle(String declarationsSectionTitle) {
		this.declarationsSectionTitle = declarationsSectionTitle;
	}

	/**
	 * @return the declarationTaxNoticeText
	 */
	public String getDeclarationTaxNoticeText() {
		return declarationTaxNoticeText;
	}

	/**
	 * @param declarationTaxNoticeText the declarationTaxNoticeText to set
	 */
	public void setDeclarationTaxNoticeText(String declarationTaxNoticeText) {
		this.declarationTaxNoticeText = declarationTaxNoticeText;
	}

	/**
	 * @return the declarationTaxNoticeLinkEzk
	 */
	public String getDeclarationTaxNoticeLinkEzk() {
		return declarationTaxNoticeLinkEzk;
	}

	/**
	 * @param declarationTaxNoticeLinkEzk the declarationTaxNoticeLinkEzk to set
	 */
	public void setDeclarationTaxNoticeLinkEzk(String declarationTaxNoticeLinkEzk) {
		this.declarationTaxNoticeLinkEzk = declarationTaxNoticeLinkEzk;
	}

	/**
	 * @return the declarationTaxNoticeLinkPsw
	 */
	public String getDeclarationTaxNoticeLinkPsw() {
		return declarationTaxNoticeLinkPsw;
	}

	/**
	 * @param declarationTaxNoticeLinkPsw the declarationTaxNoticeLinkPsw to set
	 */
	public void setDeclarationTaxNoticeLinkPsw(String declarationTaxNoticeLinkPsw) {
		this.declarationTaxNoticeLinkPsw = declarationTaxNoticeLinkPsw;
	}

	/**
	 * @return the waitingPeriodText
	 */
	public String getWaitingPeriodText() {
		return waitingPeriodText;
	}

	/**
	 * @param waitingPeriodText the waitingPeriodText to set
	 */
	public void setWaitingPeriodText(String waitingPeriodText) {
		this.waitingPeriodText = waitingPeriodText;
	}

	/**
	 * @return the iraProviderText
	 */
	public String getIraProviderText() {
		return iraProviderText;
	}

	/**
	 * @param iraProviderText the iraProviderText to set
	 */
	public void setIraProviderText(String iraProviderText) {
		this.iraProviderText = iraProviderText;
	}

	/**
	 * @return the legaleseText
	 */
	public String getLegaleseText() {
		return legaleseText;
	}

	/**
	 * @param legaleseText the legaleseText to set
	 */
	public void setLegaleseText(String legaleseText) {
		this.legaleseText = legaleseText;
	}

	/**
	 * @return the participantLegalese
	 */
	public String getParticipantLegalese() {
		return participantLegalese;
	}

	/**
	 * @param participantLegalese the participantLegalese to set
	 */
	public void setParticipantLegalese(String participantLegalese) {
		this.participantLegalese = participantLegalese;
	}

	/**
	 * @return the riskIndicatorText
	 */
	public String getRiskIndicatorText() {
		return riskIndicatorText;
	}

	/**
	 * @param riskIndicatorText the riskIndicatorText to set
	 */
	public void setRiskIndicatorText(String riskIndicatorText) {
		this.riskIndicatorText = riskIndicatorText;
	}

	/**
	 * @return the loanInformation
	 */
	public String getLoanInformation() {
		return loanInformation;
	}

	/**
	 * @param loanInformation the loanInformation to set
	 */
	public void setLoanInformation(String loanInformation) {
		this.loanInformation = loanInformation;
	}

	/**
	 * @return the step1PageBeanBody1
	 */
	public String getStep1PageBeanBody1() {
		return step1PageBeanBody1;
	}

	/**
	 * @param step1PageBeanBody1 the step1PageBeanBody1 to set
	 */
	public void setStep1PageBeanBody1(String step1PageBeanBody1) {
		this.step1PageBeanBody1 = step1PageBeanBody1;
	}

	/**
	 * @return the step1PageBeanBody2
	 */
	public String getStep1PageBeanBody2() {
		return step1PageBeanBody2;
	}

	/**
	 * @param step1PageBeanBody2 the step1PageBeanBody2 to set
	 */
	public void setStep1PageBeanBody2(String step1PageBeanBody2) {
		this.step1PageBeanBody2 = step1PageBeanBody2;
	}

	/**
	 * @return the step1PageBeanBody3
	 */
	public String getStep1PageBeanBody3() {
		return step1PageBeanBody3;
	}

	/**
	 * @param step1PageBeanBody3 the step1PageBeanBody3 to set
	 */
	public void setStep1PageBeanBody3(String step1PageBeanBody3) {
		this.step1PageBeanBody3 = step1PageBeanBody3;
	}
	
	/**
	 * @return the step2PageBeanBody3Header
	 */
	public String getStep2PageBeanBody3Header() {
		return step2PageBeanBody3Header;
	}

	/**
	 * @param step2PageBeanBody3Header the step2PageBeanBody3Header to set
	 */
	public void setStep2PageBeanBody3Header(String step2PageBeanBody3Header) {
		this.step2PageBeanBody3Header = step2PageBeanBody3Header;
	}

	/**
	 * @return the step2PageBeanBody1
	 */
	public String getStep2PageBeanBody1() {
		return step2PageBeanBody1;
	}

	/**
	 * @param step2PageBeanBody1 the step2PageBeanBody1 to set
	 */
	public void setStep2PageBeanBody1(String step2PageBeanBody1) {
		this.step2PageBeanBody1 = step2PageBeanBody1;
	}

	/**
	 * @return the step2PageBeanBody2
	 */
	public String getStep2PageBeanBody2() {
		return step2PageBeanBody2;
	}

	/**
	 * @param step2PageBeanBody2 the step2PageBeanBody2 to set
	 */
	public void setStep2PageBeanBody2(String step2PageBeanBody2) {
		this.step2PageBeanBody2 = step2PageBeanBody2;
	}

	/**
	 * @return the step2PageBeanBody3
	 */
	public String getStep2PageBeanBody3() {
		return step2PageBeanBody3;
	}

	/**
	 * @param step2PageBeanBody3 the step2PageBeanBody3 to set
	 */
	public void setStep2PageBeanBody3(String step2PageBeanBody3) {
		this.step2PageBeanBody3 = step2PageBeanBody3;
	}

	/**
	 * @return the notesSectionTitle
	 */
	public String getNotesSectionTitle() {
		return notesSectionTitle;
	}

	/**
	 * @param notesSectionTitle the notesSectionTitle to set
	 */
	public void setNotesSectionTitle(String notesSectionTitle) {
		this.notesSectionTitle = notesSectionTitle;
	}

	/**
	 * @return the notesSpecialContent
	 */
	public String getNotesSpecialContent() {
		return notesSpecialContent;
	}

	/**
	 * @param notesSpecialContent the notesSpecialContent to set
	 */
	public void setNotesSpecialContent(String notesSpecialContent) {
		this.notesSpecialContent = notesSpecialContent;
	}

	/**
	 * @return the personalInformation
	 */
	public String getPersonalInformation() {
		return personalInformation;
	}

	/**
	 * @param personalInformation the personalInformation to set
	 */
	public void setPersonalInformation(String personalInformation) {
		this.personalInformation = personalInformation;
	}

	/**
	 * @return the employeeSnapshot
	 */
	public String getEmployeeSnapshot() {
		return employeeSnapshot;
	}

	/**
	 * @param employeeSnapshot the employeeSnapshot to set
	 */
	public void setEmployeeSnapshot(String employeeSnapshot) {
		this.employeeSnapshot = employeeSnapshot;
	}

	/**
	 * @return the paymentInstructionsSectionTitle
	 */
	public String getPaymentInstructionsSectionTitle() {
		return paymentInstructionsSectionTitle;
	}

	/**
	 * @param paymentInstructionsSectionTitle the paymentInstructionsSectionTitle to set
	 */
	public void setPaymentInstructionsSectionTitle(
			String paymentInstructionsSectionTitle) {
		this.paymentInstructionsSectionTitle = paymentInstructionsSectionTitle;
	}

	/**
	 * @return the eftPayeeSectionTitle
	 */
	public String getEftPayeeSectionTitle() {
		return eftPayeeSectionTitle;
	}

	/**
	 * @param eftPayeeSectionTitle the eftPayeeSectionTitle to set
	 */
	public void setEftPayeeSectionTitle(String eftPayeeSectionTitle) {
		this.eftPayeeSectionTitle = eftPayeeSectionTitle;
	}

	/**
	 * @return the chequePayeeSectionTitle
	 */
	public String getChequePayeeSectionTitle() {
		return chequePayeeSectionTitle;
	}

	/**
	 * @param chequePayeeSectionTitle the chequePayeeSectionTitle to set
	 */
	public void setChequePayeeSectionTitle(String chequePayeeSectionTitle) {
		this.chequePayeeSectionTitle = chequePayeeSectionTitle;
	}

	/**
	 * @return the overrideCsfMailText
	 */
	public String getOverrideCsfMailText() {
		return overrideCsfMailText;
	}

	/**
	 * @param overrideCsfMailText the overrideCsfMailText to set
	 */
	public void setOverrideCsfMailText(String overrideCsfMailText) {
		this.overrideCsfMailText = overrideCsfMailText;
	}

	/**
	 * @return the payeexSectionContent
	 */
	public String getPayeexSectionContent() {
		return payeexSectionContent;
	}

	/**
	 * @param payeexSectionContent the payeexSectionContent to set
	 */
	public void setPayeexSectionContent(String payeexSectionContent) {
		this.payeexSectionContent = payeexSectionContent;
	}

	/**
	 * @return the withdrawalAmountSectionTitle
	 */
	public String getWithdrawalAmountSectionTitle() {
		return withdrawalAmountSectionTitle;
	}

	/**
	 * @param withdrawalAmountSectionTitle the withdrawalAmountSectionTitle to set
	 */
	public void setWithdrawalAmountSectionTitle(String withdrawalAmountSectionTitle) {
		this.withdrawalAmountSectionTitle = withdrawalAmountSectionTitle;
	}

	/**
	 * @return the unvestedMoneySectionContent
	 */
	public String getUnvestedMoneySectionContent() {
		return unvestedMoneySectionContent;
	}

	/**
	 * @param unvestedMoneySectionContent the unvestedMoneySectionContent to set
	 */
	public void setUnvestedMoneySectionContent(String unvestedMoneySectionContent) {
		this.unvestedMoneySectionContent = unvestedMoneySectionContent;
	}

	/**
	 * @return the vestingInformationText
	 */
	public String getVestingInformationText() {
		return vestingInformationText;
	}

	/**
	 * @param vestingInformationText the vestingInformationText to set
	 */
	public void setVestingInformationText(String vestingInformationText) {
		this.vestingInformationText = vestingInformationText;
	}

	/**
	 * @return the taxWithholdingSectionTitle
	 */
	public String getTaxWithholdingSectionTitle() {
		return taxWithholdingSectionTitle;
	}

	/**
	 * @param taxWithholdingSectionTitle the taxWithholdingSectionTitle to set
	 */
	public void setTaxWithholdingSectionTitle(String taxWithholdingSectionTitle) {
		this.taxWithholdingSectionTitle = taxWithholdingSectionTitle;
	}

	/**
	 * @return the participantHasPartialStatusText
	 */
	public String getParticipantHasPartialStatusText() {
		return participantHasPartialStatusText;
	}

	/**
	 * @param participantHasPartialStatusText the participantHasPartialStatusText to set
	 */
	public void setParticipantHasPartialStatusText(
			String participantHasPartialStatusText) {
		this.participantHasPartialStatusText = participantHasPartialStatusText;
	}

	/**
	 * @return the participantHasPre1987MoneyTypeText
	 */
	public String getParticipantHasPre1987MoneyTypeText() {
		return participantHasPre1987MoneyTypeText;
	}

	/**
	 * @param participantHasPre1987MoneyTypeText the participantHasPre1987MoneyTypeText to set
	 */
	public void setParticipantHasPre1987MoneyTypeText(
			String participantHasPre1987MoneyTypeText) {
		this.participantHasPre1987MoneyTypeText = participantHasPre1987MoneyTypeText;
	}

	/**
	 * @return the withdrawalReasonIsFullyVestedText
	 */
	public String getWithdrawalReasonIsFullyVestedText() {
		return withdrawalReasonIsFullyVestedText;
	}

	/**
	 * @param withdrawalReasonIsFullyVestedText the withdrawalReasonIsFullyVestedText to set
	 */
	public void setWithdrawalReasonIsFullyVestedText(
			String withdrawalReasonIsFullyVestedText) {
		this.withdrawalReasonIsFullyVestedText = withdrawalReasonIsFullyVestedText;
	}

	/**
	 * @return the basicInformation
	 */
	public String getBasicInformation() {
		return basicInformation;
	}

	/**
	 * @param basicInformation the basicInformation to set
	 */
	public void setBasicInformation(String basicInformation) {
		this.basicInformation = basicInformation;
	}

	/**
	 * @return the lastProcessedDateCommentText
	 */
	public String getLastProcessedDateCommentText() {
		return lastProcessedDateCommentText;
	}

	/**
	 * @param lastProcessedDateCommentText the lastProcessedDateCommentText to set
	 */
	public void setLastProcessedDateCommentText(String lastProcessedDateCommentText) {
		this.lastProcessedDateCommentText = lastProcessedDateCommentText;
	}

	/**
	 * @return the confirmationText1
	 */
	public String getConfirmationText1() {
		return confirmationText1;
	}

	/**
	 * @param confirmationText1 the confirmationText1 to set
	 */
	public void setConfirmationText1(String confirmationText1) {
		this.confirmationText1 = confirmationText1;
	}

	/**
	 * @return the confirmationText2
	 */
	public String getConfirmationText2() {
		return confirmationText2;
	}

	/**
	 * @param confirmationText2 the confirmationText2 to set
	 */
	public void setConfirmationText2(String confirmationText2) {
		this.confirmationText2 = confirmationText2;
	}

	/**
	 * @return the confirmationText3
	 */
	public String getConfirmationText3() {
		return confirmationText3;
	}

	/**
	 * @param confirmationText3 the confirmationText3 to set
	 */
	public void setConfirmationText3(String confirmationText3) {
		this.confirmationText3 = confirmationText3;
	}

	/**
	 * @return the footer1
	 */
	public String getFooter1() {
		return footer1;
	}

	/**
	 * @param footer1 the footer1 to set
	 */
	public void setFooter1(String footer1) {
		this.footer1 = footer1;
	}

	/**
	 * @return the disclaimer
	 */
	public List<String> getDisclaimer() {
		return disclaimer;
	}

	/**
	 * @param disclaimer the disclaimer to set
	 */
	public void setDisclaimer(List<String> disclaimer) {
		this.disclaimer = disclaimer;
	}

	/**
	 * @return the footnotes
	 */
	public List<String> getFootnotes() {
		return footnotes;
	}

	/**
	 * @param footnotes the footnotes to set
	 */
	public void setFootnotes(List<String> footnotes) {
		this.footnotes = footnotes;
	}

	/**
	 * @return the participantInitiatedRequestMessage
	 */
	public String getParticipantInitiatedRequestMessage() {
		return participantInitiatedRequestMessage;
	}

	/**
	 * @param participantInitiatedRequestMessage the participantInitiatedRequestMessage to set
	 */
	public void setParticipantInitiatedRequestMessage(
			String participantInitiatedRequestMessage) {
		this.participantInitiatedRequestMessage = participantInitiatedRequestMessage;
	}

	/**
	 * @return the psTPAInitiatedRequestMessage
	 */
	public String getPsTPAInitiatedRequestMessage() {
		return psTPAInitiatedRequestMessage;
	}

	/**
	 * @param psTPAInitiatedRequestMessage the psTPAInitiatedRequestMessage to set
	 */
	public void setPsTPAInitiatedRequestMessage(String psTPAInitiatedRequestMessage) {
		this.psTPAInitiatedRequestMessage = psTPAInitiatedRequestMessage;
	}

	/**
	 * @return the maskedAccountNumber
	 */
	public String getMaskedAccountNumber() {
		return maskedAccountNumber;
	}

	/**
	 * @param maskedAccountNumber the maskedAccountNumber to set
	 */
	public void setMaskedAccountNumber(String maskedAccountNumber) {
		this.maskedAccountNumber = maskedAccountNumber;
	}

	/**
	 * @return the step2PageBeanBody1HeaderList
	 */
	public List<String> getStep2PageBeanBody1HeaderList() {
		return step2PageBeanBody1HeaderList;
	}

	/**
	 * @param step2PageBeanBody1HeaderList the step2PageBeanBody1HeaderList to set
	 */
	public void setStep2PageBeanBody1HeaderList(
			List<String> step2PageBeanBody1HeaderList) {
		this.step2PageBeanBody1HeaderList = step2PageBeanBody1HeaderList;
	}

	/**
	 * @return the step2PageBeanBody2HeaderList
	 */
	public List<String> getStep2PageBeanBody2HeaderList() {
		return step2PageBeanBody2HeaderList;
	}

	/**
	 * @param step2PageBeanBody2HeaderList the step2PageBeanBody2HeaderList to set
	 */
	public void setStep2PageBeanBody2HeaderList(
			List<String> step2PageBeanBody2HeaderList) {
		this.step2PageBeanBody2HeaderList = step2PageBeanBody2HeaderList;
	}

}
