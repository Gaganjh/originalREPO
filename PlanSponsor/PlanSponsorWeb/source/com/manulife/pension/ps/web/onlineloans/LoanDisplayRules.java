package com.manulife.pension.ps.web.onlineloans;

import java.io.Serializable;
import java.util.Map;

/**
 * Display rules for online loans used only for Print PDF
 * 
 * @author ayyalsa
 *
 */
public class LoanDisplayRules implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean printFriendly = true;
	private boolean displayPageContentNotFinalDisclaimer;
	private boolean displayGiflMsgExternalUserInitiated;
	private boolean displayGiflMsgParticipantInitiated;
	private boolean displaySubmissionNumber;
	private boolean displaySubmissionStatus;
	private boolean displaySubmissionProcessingDates;
	private boolean displayNotesViewSection;
	private boolean displayPaymentInstructionSection;
	private boolean showDeclarationsSection;
	private boolean displayNotesEditSection;
	private boolean displayParticipantDeclarationCheckbox;
	private boolean displayApproverAgreedToLabel;
	private boolean displayAtRiskTransactionCheckbox;
	private boolean displayLoanCalculationBlankColumn;
	private boolean displayLoanCalculationEditable;
	private boolean displayLoanCalculationAcceptedColumn;
	private boolean displayLoanCalculationReviewedColumn;
	private boolean displayLoanCalculationOriginalColumn;
	private boolean loanAmountDisplayOnlyRecalculated;
	private boolean loanAmountDisplayOnly;
	private boolean displayTpaLoanIssueFee;
	private boolean displayDefaultProvisionExplanation;
	private boolean displayNotesToAdministrators;
	private boolean displayNoteToParticipantPrintContentText;
	private boolean displayViewNotesSectionFooter;
	private boolean displayMiddleInitial;
	private boolean maskSsn;
	private boolean displayLegallyMarried;
	private boolean displaySpousalConsentText;
	private boolean countryUSA;
	private boolean showMaskedAccountNumber;
	private boolean showBankInformationAsEditable;
	private boolean loanCalculationEditable;
	private boolean displayApproverAgreedToText;
	private boolean loanAmountEditable;
	
	private String accountBalanceLabel;
//	vestingPercentageActivityHistoryDisplayMap
	private String loanApprovalPlanSpousalConsentContent;
	private String loanApprovalGenericContent;
	private String loanCalculationOriginalColumnHeader;	
	private String loanCalculationEditableColumnHeader;
	private String loanCalculationAcceptedColumnHeader;
	private String ParticipantLabelText;
	private String loanCalculationReviewedColumnHeader;
	private String stateName;
	private String countryName;
	private String abaRountingNumber;
	
	private Map<String, Boolean> vestingPercentageActivityHistoryDisplayMap = null;
	
	/**
	 * @return the printFriendly
	 */
	public boolean isPrintFriendly() {
		return printFriendly;
	}
	/**
	 * @param printFriendly the printFriendly to set
	 */
	public void setPrintFriendly(boolean printFriendly) {
		this.printFriendly = printFriendly;
	}
	/**
	 * @return the displayPageContentNotFinalDisclaimer
	 */
	public boolean isDisplayPageContentNotFinalDisclaimer() {
		return displayPageContentNotFinalDisclaimer;
	}
	/**
	 * @param displayPageContentNotFinalDisclaimer the displayPageContentNotFinalDisclaimer to set
	 */
	public void setDisplayPageContentNotFinalDisclaimer(
			boolean displayPageContentNotFinalDisclaimer) {
		this.displayPageContentNotFinalDisclaimer = displayPageContentNotFinalDisclaimer;
	}
	/**
	 * @return the displayGiflMsgExternalUserInitiated
	 */
	public boolean isDisplayGiflMsgExternalUserInitiated() {
		return displayGiflMsgExternalUserInitiated;
	}
	/**
	 * @param displayGiflMsgExternalUserInitiated the displayGiflMsgExternalUserInitiated to set
	 */
	public void setDisplayGiflMsgExternalUserInitiated(
			boolean displayGiflMsgExternalUserInitiated) {
		this.displayGiflMsgExternalUserInitiated = displayGiflMsgExternalUserInitiated;
	}
	/**
	 * @return the displayGiflMsgParticipantInitiated
	 */
	public boolean isDisplayGiflMsgParticipantInitiated() {
		return displayGiflMsgParticipantInitiated;
	}
	/**
	 * @param displayGiflMsgParticipantInitiated the displayGiflMsgParticipantInitiated to set
	 */
	public void setDisplayGiflMsgParticipantInitiated(
			boolean displayGiflMsgParticipantInitiated) {
		this.displayGiflMsgParticipantInitiated = displayGiflMsgParticipantInitiated;
	}
	/**
	 * @return the displaySubmissionNumber
	 */
	public boolean isDisplaySubmissionNumber() {
		return displaySubmissionNumber;
	}
	/**
	 * @param displaySubmissionNumber the displaySubmissionNumber to set
	 */
	public void setDisplaySubmissionNumber(boolean displaySubmissionNumber) {
		this.displaySubmissionNumber = displaySubmissionNumber;
	}
	/**
	 * @return the displaySubmissionStatus
	 */
	public boolean isDisplaySubmissionStatus() {
		return displaySubmissionStatus;
	}
	/**
	 * @param displaySubmissionStatus the displaySubmissionStatus to set
	 */
	public void setDisplaySubmissionStatus(boolean displaySubmissionStatus) {
		this.displaySubmissionStatus = displaySubmissionStatus;
	}
	/**
	 * @return the displaySubmissionProcessingDates
	 */
	public boolean isDisplaySubmissionProcessingDates() {
		return displaySubmissionProcessingDates;
	}
	/**
	 * @param displaySubmissionProcessingDates the displaySubmissionProcessingDates to set
	 */
	public void setDisplaySubmissionProcessingDates(
			boolean displaySubmissionProcessingDates) {
		this.displaySubmissionProcessingDates = displaySubmissionProcessingDates;
	}
	/**
	 * @return the displayNotesViewSection
	 */
	public boolean isDisplayNotesViewSection() {
		return displayNotesViewSection;
	}
	/**
	 * @param displayNotesViewSection the displayNotesViewSection to set
	 */
	public void setDisplayNotesViewSection(boolean displayNotesViewSection) {
		this.displayNotesViewSection = displayNotesViewSection;
	}
	/**
	 * @return the displayPaymentInstructionSection
	 */
	public boolean isDisplayPaymentInstructionSection() {
		return displayPaymentInstructionSection;
	}
	/**
	 * @param displayPaymentInstructionSection the displayPaymentInstructionSection to set
	 */
	public void setDisplayPaymentInstructionSection(
			boolean displayPaymentInstructionSection) {
		this.displayPaymentInstructionSection = displayPaymentInstructionSection;
	}
	/**
	 * @return the showDeclarationsSection
	 */
	public boolean isShowDeclarationsSection() {
		return showDeclarationsSection;
	}
	/**
	 * @param showDeclarationsSection the showDeclarationsSection to set
	 */
	public void setShowDeclarationsSection(boolean showDeclarationsSection) {
		this.showDeclarationsSection = showDeclarationsSection;
	}
	/**
	 * @return the displayNotesEditSection
	 */
	public boolean isDisplayNotesEditSection() {
		return displayNotesEditSection;
	}
	/**
	 * @param displayNotesEditSection the displayNotesEditSection to set
	 */
	public void setDisplayNotesEditSection(boolean displayNotesEditSection) {
		this.displayNotesEditSection = displayNotesEditSection;
	}
	/**
	 * @return the displayParticipantDeclarationCheckbox
	 */
	public boolean isDisplayParticipantDeclarationCheckbox() {
		return displayParticipantDeclarationCheckbox;
	}
	/**
	 * @param displayParticipantDeclarationCheckbox the displayParticipantDeclarationCheckbox to set
	 */
	public void setDisplayParticipantDeclarationCheckbox(
			boolean displayParticipantDeclarationCheckbox) {
		this.displayParticipantDeclarationCheckbox = displayParticipantDeclarationCheckbox;
	}
	/**
	 * @return the displayApproverAgreedToLabel
	 */
	public boolean isDisplayApproverAgreedToLabel() {
		return displayApproverAgreedToLabel;
	}
	/**
	 * @param displayApproverAgreedToLabel the displayApproverAgreedToLabel to set
	 */
	public void setDisplayApproverAgreedToLabel(boolean displayApproverAgreedToLabel) {
		this.displayApproverAgreedToLabel = displayApproverAgreedToLabel;
	}
	/**
	 * @return the displayAtRiskTransactionCheckbox
	 */
	public boolean isDisplayAtRiskTransactionCheckbox() {
		return displayAtRiskTransactionCheckbox;
	}
	/**
	 * @param displayAtRiskTransactionCheckbox the displayAtRiskTransactionCheckbox to set
	 */
	public void setDisplayAtRiskTransactionCheckbox(
			boolean displayAtRiskTransactionCheckbox) {
		this.displayAtRiskTransactionCheckbox = displayAtRiskTransactionCheckbox;
	}
	/**
	 * @return the displayLoanCalculationBlankColumn
	 */
	public boolean isDisplayLoanCalculationBlankColumn() {
		return displayLoanCalculationBlankColumn;
	}
	/**
	 * @param displayLoanCalculationBlankColumn the displayLoanCalculationBlankColumn to set
	 */
	public void setDisplayLoanCalculationBlankColumn(
			boolean displayLoanCalculationBlankColumn) {
		this.displayLoanCalculationBlankColumn = displayLoanCalculationBlankColumn;
	}
	/**
	 * @return the displayLoanCalculationEditable
	 */
	public boolean isDisplayLoanCalculationEditable() {
		return displayLoanCalculationEditable;
	}
	/**
	 * @param displayLoanCalculationEditable the displayLoanCalculationEditable to set
	 */
	public void setDisplayLoanCalculationEditable(
			boolean displayLoanCalculationEditable) {
		this.displayLoanCalculationEditable = displayLoanCalculationEditable;
	}
	/**
	 * @return the displayLoanCalculationAcceptedColumn
	 */
	public boolean isDisplayLoanCalculationAcceptedColumn() {
		return displayLoanCalculationAcceptedColumn;
	}
	/**
	 * @param displayLoanCalculationAcceptedColumn the displayLoanCalculationAcceptedColumn to set
	 */
	public void setDisplayLoanCalculationAcceptedColumn(
			boolean displayLoanCalculationAcceptedColumn) {
		this.displayLoanCalculationAcceptedColumn = displayLoanCalculationAcceptedColumn;
	}
	/**
	 * @return the displayLoanCalculationReviewedColumn
	 */
	public boolean isDisplayLoanCalculationReviewedColumn() {
		return displayLoanCalculationReviewedColumn;
	}
	/**
	 * @param displayLoanCalculationReviewedColumn the displayLoanCalculationReviewedColumn to set
	 */
	public void setDisplayLoanCalculationReviewedColumn(
			boolean displayLoanCalculationReviewedColumn) {
		this.displayLoanCalculationReviewedColumn = displayLoanCalculationReviewedColumn;
	}
	/**
	 * @return the displayLoanCalculationOriginalColumn
	 */
	public boolean isDisplayLoanCalculationOriginalColumn() {
		return displayLoanCalculationOriginalColumn;
	}
	/**
	 * @param displayLoanCalculationOriginalColumn the displayLoanCalculationOriginalColumn to set
	 */
	public void setDisplayLoanCalculationOriginalColumn(
			boolean displayLoanCalculationOriginalColumn) {
		this.displayLoanCalculationOriginalColumn = displayLoanCalculationOriginalColumn;
	}
	/**
	 * @return the loanAmountDisplayOnlyRecalculated
	 */
	public boolean isLoanAmountDisplayOnlyRecalculated() {
		return loanAmountDisplayOnlyRecalculated;
	}
	/**
	 * @param loanAmountDisplayOnlyRecalculated the loanAmountDisplayOnlyRecalculated to set
	 */
	public void setLoanAmountDisplayOnlyRecalculated(
			boolean loanAmountDisplayOnlyRecalculated) {
		this.loanAmountDisplayOnlyRecalculated = loanAmountDisplayOnlyRecalculated;
	}
	/**
	 * @return the loanAmountDisplayOnly
	 */
	public boolean isLoanAmountDisplayOnly() {
		return loanAmountDisplayOnly;
	}
	/**
	 * @param loanAmountDisplayOnly the loanAmountDisplayOnly to set
	 */
	public void setLoanAmountDisplayOnly(boolean loanAmountDisplayOnly) {
		this.loanAmountDisplayOnly = loanAmountDisplayOnly;
	}
	/**
	 * @return the displayTpaLoanIssueFee
	 */
	public boolean isDisplayTpaLoanIssueFee() {
		return displayTpaLoanIssueFee;
	}
	/**
	 * @param displayTpaLoanIssueFee the displayTpaLoanIssueFee to set
	 */
	public void setDisplayTpaLoanIssueFee(boolean displayTpaLoanIssueFee) {
		this.displayTpaLoanIssueFee = displayTpaLoanIssueFee;
	}
	/**
	 * @return the displayDefaultProvisionExplanation
	 */
	public boolean isDisplayDefaultProvisionExplanation() {
		return displayDefaultProvisionExplanation;
	}
	/**
	 * @param displayDefaultProvisionExplanation the displayDefaultProvisionExplanation to set
	 */
	public void setDisplayDefaultProvisionExplanation(
			boolean displayDefaultProvisionExplanation) {
		this.displayDefaultProvisionExplanation = displayDefaultProvisionExplanation;
	}
	/**
	 * @return the displayNotesToAdministrators
	 */
	public boolean isDisplayNotesToAdministrators() {
		return displayNotesToAdministrators;
	}
	/**
	 * @param displayNotesToAdministrators the displayNotesToAdministrators to set
	 */
	public void setDisplayNotesToAdministrators(boolean displayNotesToAdministrators) {
		this.displayNotesToAdministrators = displayNotesToAdministrators;
	}
	/**
	 * @return the displayNoteToParticipantPrintContentText
	 */
	public boolean isDisplayNoteToParticipantPrintContentText() {
		return displayNoteToParticipantPrintContentText;
	}
	/**
	 * @param displayNoteToParticipantPrintContentText the displayNoteToParticipantPrintContentText to set
	 */
	public void setDisplayNoteToParticipantPrintContentText(
			boolean displayNoteToParticipantPrintContentText) {
		this.displayNoteToParticipantPrintContentText = displayNoteToParticipantPrintContentText;
	}
	/**
	 * @return the displayViewNotesSectionFooter
	 */
	public boolean isDisplayViewNotesSectionFooter() {
		return displayViewNotesSectionFooter;
	}
	/**
	 * @param displayViewNotesSectionFooter the displayViewNotesSectionFooter to set
	 */
	public void setDisplayViewNotesSectionFooter(
			boolean displayViewNotesSectionFooter) {
		this.displayViewNotesSectionFooter = displayViewNotesSectionFooter;
	}
	/**
	 * @return the displayMiddleInitial
	 */
	public boolean isDisplayMiddleInitial() {
		return displayMiddleInitial;
	}
	/**
	 * @param displayMiddleInitial the displayMiddleInitial to set
	 */
	public void setDisplayMiddleInitial(boolean displayMiddleInitial) {
		this.displayMiddleInitial = displayMiddleInitial;
	}
	/**
	 * @return the maskSsn
	 */
	public boolean isMaskSsn() {
		return maskSsn;
	}
	/**
	 * @param maskSsn the maskSsn to set
	 */
	public void setMaskSsn(boolean maskSsn) {
		this.maskSsn = maskSsn;
	}
	/**
	 * @return the displayLegallyMarried
	 */
	public boolean isDisplayLegallyMarried() {
		return displayLegallyMarried;
	}
	/**
	 * @param displayLegallyMarried the displayLegallyMarried to set
	 */
	public void setDisplayLegallyMarried(boolean displayLegallyMarried) {
		this.displayLegallyMarried = displayLegallyMarried;
	}
	/**
	 * @return the displaySpousalConsentText
	 */
	public boolean isDisplaySpousalConsentText() {
		return displaySpousalConsentText;
	}
	/**
	 * @param displaySpousalConsentText the displaySpousalConsentText to set
	 */
	public void setDisplaySpousalConsentText(boolean displaySpousalConsentText) {
		this.displaySpousalConsentText = displaySpousalConsentText;
	}
	/**
	 * @return the countryUSA
	 */
	public boolean isCountryUSA() {
		return countryUSA;
	}
	/**
	 * @param countryUSA the countryUSA to set
	 */
	public void setCountryUSA(boolean countryUSA) {
		this.countryUSA = countryUSA;
	}
	/**
	 * @return the showMaskedAccountNumber
	 */
	public boolean isShowMaskedAccountNumber() {
		return showMaskedAccountNumber;
	}
	/**
	 * @param showMaskedAccountNumber the showMaskedAccountNumber to set
	 */
	public void setShowMaskedAccountNumber(boolean showMaskedAccountNumber) {
		this.showMaskedAccountNumber = showMaskedAccountNumber;
	}
	/**
	 * @return the showBankInformationAsEditable
	 */
	public boolean isShowBankInformationAsEditable() {
		return showBankInformationAsEditable;
	}
	/**
	 * @param showBankInformationAsEditable the showBankInformationAsEditable to set
	 */
	public void setShowBankInformationAsEditable(
			boolean showBankInformationAsEditable) {
		this.showBankInformationAsEditable = showBankInformationAsEditable;
	}
	/**
	 * @return the accountBalanceLabel
	 */
	public String getAccountBalanceLabel() {
		return accountBalanceLabel;
	}
	/**
	 * @param accountBalanceLabel the accountBalanceLabel to set
	 */
	public void setAccountBalanceLabel(String accountBalanceLabel) {
		this.accountBalanceLabel = accountBalanceLabel;
	}
	/**
	 * @return the loanApprovalPlanSpousalConsentContent
	 */
	public String getLoanApprovalPlanSpousalConsentContent() {
		return loanApprovalPlanSpousalConsentContent;
	}
	/**
	 * @param loanApprovalPlanSpousalConsentContent the loanApprovalPlanSpousalConsentContent to set
	 */
	public void setLoanApprovalPlanSpousalConsentContent(
			String loanApprovalPlanSpousalConsentContent) {
		this.loanApprovalPlanSpousalConsentContent = loanApprovalPlanSpousalConsentContent;
	}
	/**
	 * @return the loanApprovalGenericContent
	 */
	public String getLoanApprovalGenericContent() {
		return loanApprovalGenericContent;
	}
	/**
	 * @param loanApprovalGenericContent the loanApprovalGenericContent to set
	 */
	public void setLoanApprovalGenericContent(String loanApprovalGenericContent) {
		this.loanApprovalGenericContent = loanApprovalGenericContent;
	}
	/**
	 * @return the loanCalculationOriginalColumnHeader
	 */
	public String getLoanCalculationOriginalColumnHeader() {
		return loanCalculationOriginalColumnHeader;
	}
	/**
	 * @param loanCalculationOriginalColumnHeader the loanCalculationOriginalColumnHeader to set
	 */
	public void setLoanCalculationOriginalColumnHeader(
			String loanCalculationOriginalColumnHeader) {
		this.loanCalculationOriginalColumnHeader = loanCalculationOriginalColumnHeader;
	}
	/**
	 * @return the loanCalculationEditableColumnHeader
	 */
	public String getLoanCalculationEditableColumnHeader() {
		return loanCalculationEditableColumnHeader;
	}
	/**
	 * @param loanCalculationEditableColumnHeader the loanCalculationEditableColumnHeader to set
	 */
	public void setLoanCalculationEditableColumnHeader(
			String loanCalculationEditableColumnHeader) {
		this.loanCalculationEditableColumnHeader = loanCalculationEditableColumnHeader;
	}
	/**
	 * @return the loanCalculationAcceptedColumnHeader
	 */
	public String getLoanCalculationAcceptedColumnHeader() {
		return loanCalculationAcceptedColumnHeader;
	}
	/**
	 * @param loanCalculationAcceptedColumnHeader the loanCalculationAcceptedColumnHeader to set
	 */
	public void setLoanCalculationAcceptedColumnHeader(
			String loanCalculationAcceptedColumnHeader) {
		this.loanCalculationAcceptedColumnHeader = loanCalculationAcceptedColumnHeader;
	}
	/**
	 * @return the participantLabelText
	 */
	public String getParticipantLabelText() {
		return ParticipantLabelText;
	}
	/**
	 * @param participantLabelText the participantLabelText to set
	 */
	public void setParticipantLabelText(String participantLabelText) {
		ParticipantLabelText = participantLabelText;
	}
	/**
	 * @return the loanCalculationReviewedColumnHeader
	 */
	public String getLoanCalculationReviewedColumnHeader() {
		return loanCalculationReviewedColumnHeader;
	}
	/**
	 * @param loanCalculationReviewedColumnHeader the loanCalculationReviewedColumnHeader to set
	 */
	public void setLoanCalculationReviewedColumnHeader(
			String loanCalculationReviewedColumnHeader) {
		this.loanCalculationReviewedColumnHeader = loanCalculationReviewedColumnHeader;
	}
	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}
	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}
	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	/**
	 * @return the vestingPercentageActivityHistoryDisplayMap
	 */
	public Map<String, Boolean> getVestingPercentageActivityHistoryDisplayMap() {
		return vestingPercentageActivityHistoryDisplayMap;
	}
	/**
	 * @param vestingPercentageActivityHistoryDisplayMap the vestingPercentageActivityHistoryDisplayMap to set
	 */
	public void setVestingPercentageActivityHistoryDisplayMap(
			Map<String, Boolean> vestingPercentageActivityHistoryDisplayMap) {
		this.vestingPercentageActivityHistoryDisplayMap = vestingPercentageActivityHistoryDisplayMap;
	}
	/**
	 * @return the loanCalculationEditable
	 */
	public boolean isLoanCalculationEditable() {
		return loanCalculationEditable;
	}
	/**
	 * @param loanCalculationEditable the loanCalculationEditable to set
	 */
	public void setLoanCalculationEditable(boolean loanCalculationEditable) {
		this.loanCalculationEditable = loanCalculationEditable;
	}
	/**
	 * @return the displayApproverAgreedToText
	 */
	public boolean isDisplayApproverAgreedToText() {
		return displayApproverAgreedToText;
	}
	/**
	 * @param displayApproverAgreedToText the displayApproverAgreedToText to set
	 */
	public void setDisplayApproverAgreedToText(boolean displayApproverAgreedToText) {
		this.displayApproverAgreedToText = displayApproverAgreedToText;
	}
	/**
	 * @return the abaRountingNumber
	 */
	public String getAbaRountingNumber() {
		return abaRountingNumber;
	}
	/**
	 * @param abaRountingNumber the abaRountingNumber to set
	 */
	public void setAbaRountingNumber(String abaRountingNumber) {
		this.abaRountingNumber = abaRountingNumber;
	}
	/**
	 * @return the loanAmountEditable
	 */
	public boolean isLoanAmountEditable() {
		return loanAmountEditable;
	}
	/**
	 * @param loanAmountEditable the loanAmountEditable to set
	 */
	public void setLoanAmountEditable(boolean loanAmountEditable) {
		this.loanAmountEditable = loanAmountEditable;
	}

}
