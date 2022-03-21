package com.manulife.pension.ps.web.onlineloans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

/**
 * value Object for loans, this used only for Print PDFs
 * 
 * @author ayyalsa
 *
 */
public class LoanData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LoanForm loanForm;
	private LoanParticipantData loanParticipantData;
	private Loan loan;
	private LoanPlanData loanPlanData;
	private LoanDisplayRules displayRules;
	private LoanActivities loanActivities;
	private LoanMoneyTypeCalculation loanMoneyTypeCalculation;
	private LoanParameter currentLoanParameter;
	private LoansCmaContent cmaContent = new LoansCmaContent();
	private List<LoanMoneyType> moneyTypesWithAccountBalance = new ArrayList<LoanMoneyType>();
	
	private String formattedSSN;
	private String formattedMaskedSSN;
	private boolean printParticipant;
	private String maskedAccountNumber = StringUtils.EMPTY;
	
	final String unmangedImagePath = String.valueOf(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX));
			
	/**
	 * REQ_USER_NAMES = "userNames"
	 */
	private Map<Integer, UserName> userNames;
	
	/**
	 * REQ_LOAN_STATUS_CODES = "loanStatusCodes"
	 */
	private String loanStatus;
	
	/**
	 * REQ_LOAN_TYPES = "loanTypes"
	 */
	private String loanType;
	
	
	/**
	 * REQ_LOAN_PAYMENT_FREQUENCIES = "loanPaymentFrequencies"
	 */
	private Map<String, String> loanPaymentFrequencies;
	
	/**
	 * REQ_PAYMENT_METHODS = "paymentMethods"
	 */
	private Map<String, String> paymentMethods;
	
	/**
	 * REQ_STATES = "states"
	 */
	private Map<String, String> states;
	
	/**
	 * REQ_COUNTRIES = "countries"
	 */
	private Map<String, String> countries;
	
	/**
	 * REQ_BANK_ACCOUNT_TYPES = "bankAccountTypes" 
	 */
	private Map<String, String> bankAccountTypes;
	
	/**
	 * REQ_EMPLOYEE_STATUSES = "employeeStatuses"
	 */
	private String employeeStatus;

	/**
	 * @return the loanForm
	 */
	public LoanForm getLoanForm() {
		return loanForm;
	}

	/**
	 * @param loanForm the loanForm to set
	 */
	public void setLoanForm(LoanForm loanForm) {
		this.loanForm = loanForm;
	}

	/**
	 * @return the loanParticipantData
	 */
	public LoanParticipantData getLoanParticipantData() {
		return loanParticipantData;
	}

	/**
	 * @param loanParticipantData the loanParticipantData to set
	 */
	public void setLoanParticipantData(LoanParticipantData loanParticipantData) {
		this.loanParticipantData = loanParticipantData;
	}

	/**
	 * @return the loan
	 */
	public Loan getLoan() {
		return loan;
	}

	/**
	 * @param loan the loan to set
	 */
	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	/**
	 * @return the loanPlanData
	 */
	public LoanPlanData getLoanPlanData() {
		return loanPlanData;
	}

	/**
	 * @param loanPlanData the loanPlanData to set
	 */
	public void setLoanPlanData(LoanPlanData loanPlanData) {
		this.loanPlanData = loanPlanData;
	}

	/**
	 * @return the displayRules
	 */
	public LoanDisplayRules getDisplayRules() {
		return displayRules;
	}

	/**
	 * @param displayRules the displayRules to set
	 */
	public void setDisplayRules(LoanDisplayRules displayRules) {
		this.displayRules = displayRules;
	}

	/**
	 * @return the loanActivities
	 */
	public LoanActivities getLoanActivities() {
		return loanActivities;
	}

	/**
	 * @param loanActivities the loanActivities to set
	 */
	public void setLoanActivities(LoanActivities loanActivities) {
		this.loanActivities = loanActivities;
	}
	
	/**
	 * @return the loanMoneyTypeCalculation
	 */
	public LoanMoneyTypeCalculation getLoanMoneyTypeCalculation() {
		return loanMoneyTypeCalculation;
	}

	/**
	 * @param loanMoneyTypeCalculation the loanMoneyTypeCalculation to set
	 */
	public void setLoanMoneyTypeCalculation(
			LoanMoneyTypeCalculation loanMoneyTypeCalculation) {
		this.loanMoneyTypeCalculation = loanMoneyTypeCalculation;
	}

	/**
	 * @return the currentLoanParameter
	 */
	public LoanParameter getCurrentLoanParameter() {
		return currentLoanParameter;
	}

	/**
	 * @param currentLoanParameter the currentLoanParameter to set
	 */
	public void setCurrentLoanParameter(LoanParameter currentLoanParameter) {
		this.currentLoanParameter = currentLoanParameter;
	}

	/**
	 * @return the cmaContent
	 */
	public LoansCmaContent getCmaContent() {
		return cmaContent;
	}

	/**
	 * @param cmaContent the cmaContent to set
	 */
	public void setCmaContent(LoansCmaContent cmaContent) {
		this.cmaContent = cmaContent;
	}

	/**
	 * @return the formattedSSN
	 */
	public String getFormattedSSN() {
		return formattedSSN;
	}

	/**
	 * @param formattedSSN the formattedSSN to set
	 */
	public void setFormattedSSN(String formattedSSN) {
		this.formattedSSN = formattedSSN;
	}

	/**
	 * @return the formattedMaskedSSN
	 */
	public String getFormattedMaskedSSN() {
		return formattedMaskedSSN;
	}

	/**
	 * @param formattedMaskedSSN the formattedMaskedSSN to set
	 */
	public void setFormattedMaskedSSN(String formattedMaskedSSN) {
		this.formattedMaskedSSN = formattedMaskedSSN;
	}

	/**
	 * @return the userNames
	 */
	public Map<Integer, UserName> getUserNames() {
		return userNames;
	}

	/**
	 * @param userNames the userNames to set
	 */
	public void setUserNames(Map<Integer, UserName> userNames) {
		this.userNames = userNames;
	}

	/**
	 * @return the loanStatus
	 */
	public String getLoanStatus() {
		return loanStatus;
	}

	/**
	 * @param loanStatus the loanStatus to set
	 */
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	/**
	 * @return the loanType
	 */
	public String getLoanType() {
		return loanType;
	}

	/**
	 * @param loanType the loanType to set
	 */
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	/**
	 * @return the loanPaymentFrequencies
	 */
	public Map<String, String> getLoanPaymentFrequencies() {
		return loanPaymentFrequencies;
	}

	/**
	 * @param loanPaymentFrequencies the loanPaymentFrequencies to set
	 */
	public void setLoanPaymentFrequencies(Map<String, String> loanPaymentFrequencies) {
		this.loanPaymentFrequencies = loanPaymentFrequencies;
	}

	/**
	 * @return the paymentMethods
	 */
	public Map<String, String> getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods the paymentMethods to set
	 */
	public void setPaymentMethods(Map<String, String> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	/**
	 * @return the states
	 */
	public Map<String, String> getStates() {
		return states;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(Map<String, String> states) {
		this.states = states;
	}

	/**
	 * @return the countries
	 */
	public Map<String, String> getCountries() {
		return countries;
	}

	/**
	 * @param countries the countries to set
	 */
	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}

	/**
	 * @return the bankAccountTypes
	 */
	public Map<String, String> getBankAccountTypes() {
		return bankAccountTypes;
	}

	/**
	 * @param bankAccountTypes the bankAccountTypes to set
	 */
	public void setBankAccountTypes(Map<String, String> bankAccountTypes) {
		this.bankAccountTypes = bankAccountTypes;
	}

	/**
	 * @return the employeeStatus
	 */
	public String getEmployeeStatus() {
		return employeeStatus;
	}

	/**
	 * @param employeeStatus the employeeStatus to set
	 */
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	
	/**
	 * @return the printParticipant
	 */
	public boolean isPrintParticipant() {
		return printParticipant;
	}

	/**
	 * @param printParticipant the printParticipant to set
	 */
	public void setPrintParticipant(boolean printParticipant) {
		this.printParticipant = printParticipant;
	}
	
	/**
	 * @return the moneyTypesWithAccountBalance
	 */
	public List<LoanMoneyType> getMoneyTypesWithAccountBalance() {
		return moneyTypesWithAccountBalance;
	}

	/**
	 * @param moneyTypesWithAccountBalance the moneyTypesWithAccountBalance to set
	 */
	public void setMoneyTypesWithAccountBalance(
			List<LoanMoneyType> moneyTypesWithAccountBalance) {
		this.moneyTypesWithAccountBalance = moneyTypesWithAccountBalance;
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
	 * CMA Content 
	 */
	class LoansCmaContent implements Serializable {
	
		private static final long serialVersionUID = 1L;
		private String pageContentNotFinalDisclaimer = StringUtils.EMPTY;
		private String giflMsgExternalUserInitiated = StringUtils.EMPTY;
		private String giflMsgParticipantInitiated = StringUtils.EMPTY;
		private String accountBalanceFootnoteText = StringUtils.EMPTY;
		private String globalDisclosure = StringUtils.EMPTY;
		private String spousalConsentMayBeRequiredText = StringUtils.EMPTY;
		private String spousalConsentMayBeRequiredIsMarriedText = StringUtils.EMPTY;
		private String spousalConsentMustBeObtainedText = StringUtils.EMPTY;
		private String spousalConsentIsNotRequiredText = StringUtils.EMPTY;
		private String participantInfoTitleText = StringUtils.EMPTY;
		private String participantInfoFooterText = StringUtils.EMPTY;
		private String calcualteMaxAvailableForLoanSectionTitleText = StringUtils.EMPTY;
		private String vestingExplanationLinkText = StringUtils.EMPTY;
		private String calculateMaxLoanAvailableFooterText = StringUtils.EMPTY;
		private String declarationsSectionTitleText = StringUtils.EMPTY;
		private String declarationsSectionFooterText = StringUtils.EMPTY;
		private String loanCalcualtionsTitleText = StringUtils.EMPTY;
		private String defaultProvisionExplanationText = StringUtils.EMPTY;
		private String loanDetalsTitleText = StringUtils.EMPTY;
		private String noteToparticipantPrintContentText = StringUtils.EMPTY;
		private String viewNotesSectionFooterText = StringUtils.EMPTY;
		private String paymentInstructionsSectionTitleText = StringUtils.EMPTY;
		private String wireChargesContentText = StringUtils.EMPTY;
		private String paymentInstructionsFooterText = StringUtils.EMPTY;
		
		/**
		 * @return the pageContentNotFinalDisclaimer
		 */
		public String getPageContentNotFinalDisclaimer() {
			return pageContentNotFinalDisclaimer;
		}
		/**
		 * @param pageContentNotFinalDisclaimer the pageContentNotFinalDisclaimer to set
		 */
		public void setPageContentNotFinalDisclaimer(
				String pageContentNotFinalDisclaimer) {
			this.pageContentNotFinalDisclaimer = pageContentNotFinalDisclaimer;
		}
		/**
		 * @return the giflMsgExternalUserInitiated
		 */
		public String getGiflMsgExternalUserInitiated() {
			return giflMsgExternalUserInitiated;
		}
		/**
		 * @param giflMsgExternalUserInitiated the giflMsgExternalUserInitiated to set
		 */
		public void setGiflMsgExternalUserInitiated(String giflMsgExternalUserInitiated) {
			this.giflMsgExternalUserInitiated = giflMsgExternalUserInitiated;
		}
		/**
		 * @return the giflMsgParticipantInitiated
		 */
		public String getGiflMsgParticipantInitiated() {
			return giflMsgParticipantInitiated;
		}
		/**
		 * @param giflMsgParticipantInitiated the giflMsgParticipantInitiated to set
		 */
		public void setGiflMsgParticipantInitiated(String giflMsgParticipantInitiated) {
			this.giflMsgParticipantInitiated = giflMsgParticipantInitiated;
		}
		/**
		 * @return the accountBalanceFootnoteText
		 */
		public String getAccountBalanceFootnoteText() {
			return accountBalanceFootnoteText;
		}
		/**
		 * @param accountBalanceFootnoteText the accountBalanceFootnoteText to set
		 */
		public void setAccountBalanceFootnoteText(String accountBalanceFootnoteText) {
			this.accountBalanceFootnoteText = accountBalanceFootnoteText;
		}
		/**
		 * @return the globalDisclosure
		 */
		public String getGlobalDisclosure() {
			return globalDisclosure;
		}
		/**
		 * @param globalDisclosure the globalDisclosure to set
		 */
		public void setGlobalDisclosure(String globalDisclosure) {
			this.globalDisclosure = globalDisclosure;
		}
		/**
		 * @return the spousalConsentMayBeRequiredText
		 */
		public String getSpousalConsentMayBeRequiredText() {
			return spousalConsentMayBeRequiredText;
		}
		/**
		 * @param spousalConsentMayBeRequiredText the spousalConsentMayBeRequiredText to set
		 */
		public void setSpousalConsentMayBeRequiredText(
				String spousalConsentMayBeRequiredText) {
			this.spousalConsentMayBeRequiredText = spousalConsentMayBeRequiredText;
		}
		/**
		 * @return the spousalConsentMayBeRequiredIsMarriedText
		 */
		public String getSpousalConsentMayBeRequiredIsMarriedText() {
			return spousalConsentMayBeRequiredIsMarriedText;
		}
		/**
		 * @param spousalConsentMayBeRequiredIsMarriedText the spousalConsentMayBeRequiredIsMarriedText to set
		 */
		public void setSpousalConsentMayBeRequiredIsMarriedText(
				String spousalConsentMayBeRequiredIsMarriedText) {
			this.spousalConsentMayBeRequiredIsMarriedText = spousalConsentMayBeRequiredIsMarriedText;
		}
		/**
		 * @return the spousalConsentMustBeObtainedText
		 */
		public String getSpousalConsentMustBeObtainedText() {
			return spousalConsentMustBeObtainedText;
		}
		/**
		 * @param spousalConsentMustBeObtainedText the spousalConsentMustBeObtainedText to set
		 */
		public void setSpousalConsentMustBeObtainedText(
				String spousalConsentMustBeObtainedText) {
			this.spousalConsentMustBeObtainedText = spousalConsentMustBeObtainedText;
		}
		/**
		 * @return the spousalConsentIsNotRequiredText
		 */
		public String getSpousalConsentIsNotRequiredText() {
			return spousalConsentIsNotRequiredText;
		}
		/**
		 * @param spousalConsentIsNotRequiredText the spousalConsentIsNotRequiredText to set
		 */
		public void setSpousalConsentIsNotRequiredText(
				String spousalConsentIsNotRequiredText) {
			this.spousalConsentIsNotRequiredText = spousalConsentIsNotRequiredText;
		}
		/**
		 * @return the participantInfoTitleText
		 */
		public String getParticipantInfoTitleText() {
			return participantInfoTitleText;
		}
		/**
		 * @param participantInfoTitleText the participantInfoTitleText to set
		 */
		public void setParticipantInfoTitleText(String participantInfoTitleText) {
			this.participantInfoTitleText = participantInfoTitleText;
		}
		/**
		 * @return the participantInfoFooterText
		 */
		public String getParticipantInfoFooterText() {
			return participantInfoFooterText;
		}
		/**
		 * @param participantInfoFooterText the participantInfoFooterText to set
		 */
		public void setParticipantInfoFooterText(String participantInfoFooterText) {
			this.participantInfoFooterText = participantInfoFooterText;
		}
		/**
		 * @return the calcualteMaxAvailableForLoanSectionTitleText
		 */
		public String getCalcualteMaxAvailableForLoanSectionTitleText() {
			return calcualteMaxAvailableForLoanSectionTitleText;
		}
		/**
		 * @param calcualteMaxAvailableForLoanSectionTitleText the calcualteMaxAvailableForLoanSectionTitleText to set
		 */
		public void setCalcualteMaxAvailableForLoanSectionTitleText(
				String calcualteMaxAvailableForLoanSectionTitleText) {
			this.calcualteMaxAvailableForLoanSectionTitleText = calcualteMaxAvailableForLoanSectionTitleText;
		}
		/**
		 * @return the vestingExplanationLinkText
		 */
		public String getVestingExplanationLinkText() {
			return vestingExplanationLinkText;
		}
		/**
		 * @param vestingExplanationLinkText the vestingExplanationLinkText to set
		 */
		public void setVestingExplanationLinkText(String vestingExplanationLinkText) {
			this.vestingExplanationLinkText = vestingExplanationLinkText;
		}
		/**
		 * @return the calculateMaxLoanAvailableFooterText
		 */
		public String getCalculateMaxLoanAvailableFooterText() {
			return calculateMaxLoanAvailableFooterText;
		}
		/**
		 * @param calculateMaxLoanAvailableFooterText the calculateMaxLoanAvailableFooterText to set
		 */
		public void setCalculateMaxLoanAvailableFooterText(
				String calculateMaxLoanAvailableFooterText) {
			this.calculateMaxLoanAvailableFooterText = calculateMaxLoanAvailableFooterText;
		}
		/**
		 * @return the declarationsSectionTitleText
		 */
		public String getDeclarationsSectionTitleText() {
			return declarationsSectionTitleText;
		}
		/**
		 * @param declarationsSectionTitleText the declarationsSectionTitleText to set
		 */
		public void setDeclarationsSectionTitleText(String declarationsSectionTitleText) {
			this.declarationsSectionTitleText = declarationsSectionTitleText;
		}
		/**
		 * @return the declarationsSectionFooterText
		 */
		public String getDeclarationsSectionFooterText() {
			return declarationsSectionFooterText;
		}
		/**
		 * @param declarationsSectionFooterText the declarationsSectionFooterText to set
		 */
		public void setDeclarationsSectionFooterText(
				String declarationsSectionFooterText) {
			this.declarationsSectionFooterText = declarationsSectionFooterText;
		}
		/**
		 * @return the loanCalcualtionsTitleText
		 */
		public String getLoanCalcualtionsTitleText() {
			return loanCalcualtionsTitleText;
		}
		/**
		 * @param loanCalcualtionsTitleText the loanCalcualtionsTitleText to set
		 */
		public void setLoanCalcualtionsTitleText(String loanCalcualtionsTitleText) {
			this.loanCalcualtionsTitleText = loanCalcualtionsTitleText;
		}
		/**
		 * @return the defaultProvisionExplanationText
		 */
		public String getDefaultProvisionExplanationText() {
			return defaultProvisionExplanationText;
		}
		/**
		 * @param defaultProvisionExplanationText the defaultProvisionExplanationText to set
		 */
		public void setDefaultProvisionExplanationText(
				String defaultProvisionExplanationText) {
			this.defaultProvisionExplanationText = defaultProvisionExplanationText;
		}
		/**
		 * @return the loanDetalsTitleText
		 */
		public String getLoanDetalsTitleText() {
			return loanDetalsTitleText;
		}
		/**
		 * @param loanDetalsTitleText the loanDetalsTitleText to set
		 */
		public void setLoanDetalsTitleText(String loanDetalsTitleText) {
			this.loanDetalsTitleText = loanDetalsTitleText;
		}
		/**
		 * @return the noteToparticipantPrintContentText
		 */
		public String getNoteToparticipantPrintContentText() {
			return noteToparticipantPrintContentText;
		}
		/**
		 * @param noteToparticipantPrintContentText the noteToparticipantPrintContentText to set
		 */
		public void setNoteToparticipantPrintContentText(
				String noteToparticipantPrintContentText) {
			this.noteToparticipantPrintContentText = noteToparticipantPrintContentText;
		}
		/**
		 * @return the viewNotesSectionFooterText
		 */
		public String getViewNotesSectionFooterText() {
			return viewNotesSectionFooterText;
		}
		/**
		 * @param viewNotesSectionFooterText the viewNotesSectionFooterText to set
		 */
		public void setViewNotesSectionFooterText(String viewNotesSectionFooterText) {
			this.viewNotesSectionFooterText = viewNotesSectionFooterText;
		}
		/**
		 * @return the paymentInstructionsSectionTitleText
		 */
		public String getPaymentInstructionsSectionTitleText() {
			return paymentInstructionsSectionTitleText;
		}
		/**
		 * @param paymentInstructionsSectionTitleText the paymentInstructionsSectionTitleText to set
		 */
		public void setPaymentInstructionsSectionTitleText(
				String paymentInstructionsSectionTitleText) {
			this.paymentInstructionsSectionTitleText = paymentInstructionsSectionTitleText;
		}
		/**
		 * @return the wireChargesContentText
		 */
		public String getWireChargesContentText() {
			return wireChargesContentText;
		}
		/**
		 * @param wireChargesContentText the wireChargesContentText to set
		 */
		public void setWireChargesContentText(String wireChargesContentText) {
			this.wireChargesContentText = wireChargesContentText;
		}
		/**
		 * @return the paymentInstructionsFooterText
		 */
		public String getPaymentInstructionsFooterText() {
			return paymentInstructionsFooterText;
		}
		/**
		 * @param paymentInstructionsFooterText the paymentInstructionsFooterText to set
		 */
		public void setPaymentInstructionsFooterText(
				String paymentInstructionsFooterText) {
			this.paymentInstructionsFooterText = paymentInstructionsFooterText;
		}
		
	}

}
