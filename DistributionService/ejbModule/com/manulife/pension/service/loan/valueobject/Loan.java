package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanError;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.withdrawal.valueobject.Address;

public class Loan extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private Integer contractId;
	private Timestamp created;
	private Timestamp lastUpdated;
	private Integer createdId;
	private String createdByRoleCode;
	private Integer lastUpdatedId;
	private String status;
	private String previousStatus;
	private Integer submissionId;
	private Integer participantProfileId;
	private Integer participantId;
	private Boolean legallyMarriedInd;
	private String loanType ;
	private String loanReason;
	private Date requestDate;
	private Date expirationDate;
	private Date effectiveDate;
	private Date effectiveDateOriginalDBValue;
    private Date maturityDate;
    private Date maturityDateOriginalDBValue;
	private Date firstPayrollDate;
	private BigDecimal maxBalanceLast12Months;
	private Integer maximumAmortizationYears;
	private Integer outstandingLoansCount;
	private BigDecimal currentOutstandingBalance;
	private Boolean applyIrs10KDollarRuleInd;
	private List<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>();
	private List<LoanDeclaration> declarations = new ArrayList<LoanDeclaration>();
	private List<LoanNote> previousAdministratorNotes = new ArrayList<LoanNote>();
	private List<LoanNote> previousParticipantNotes = new ArrayList<LoanNote>();
	private LoanNote currentAdministratorNote;
	private LoanNote currentParticipantNote;
	private List<ManagedContent> managedContents = new ArrayList<ManagedContent>();
	private LoanParameter originalParameter;
	private LoanParameter reviewedParameter;
	private LoanParameter acceptedParameter;
	private Integer loginUserProfileId;
	private String loginRoleCode;
	private String defaultProvision;
	private Fee fee;
	private boolean isFeeChanged = false;
	private Integer lastFeeChangedByTpaProfileId;
	private Boolean lastFeeChangedWasPlanSponsorUserInd;
	private LoanRecipient recipient;
	private BigDecimal maximumLoanAmount;
	private BigDecimal minimumLoanAmount;
	private BigDecimal maximumLoanPercentage;
	private String spousalConsentReqdInd;
	private BigDecimal contractLoanMonthlyFlatFee;
	private BigDecimal contractLoanExpenseMarginPct;
	private String atRiskInd;
	private Address atRiskAddress;
	private EmployeeVestingInformation employeeVestingInformation;
	private List<LoanMessage> errors = new ArrayList<LoanMessage>();
	private List<LoanMessage> messages = new ArrayList<LoanMessage>();
	private boolean ignoreWarning;
	private LoanPlanData loanPlanData;
	private LoanParticipantData loanParticipantData;
	private LoanSettings loanSettings;
	private transient LoanSupportDataRetriever dataRetriever;
	private String emailChangePinAtRiskDays = null;
	private boolean isBundledContract = false;
	private boolean isSigningAuthorityForContractTpaFirm = false;
	private boolean declartionSectionDisplayed = false;
	private AtRiskDetailsVO atRiskDetailsVO = null;
	private UserRole userRole = null;

    public LoanSupportDataRetriever getDataRetriever() {
		return dataRetriever;
	}

	public void setDataRetriever(LoanSupportDataRetriever dataRetriever) {
		this.dataRetriever = dataRetriever;
	}

	public void setLoanPlanData(LoanPlanData loanPlanData) {
		this.loanPlanData = loanPlanData;
	}

	public void setLoanParticipantData(LoanParticipantData loanParticipantData) {
		this.loanParticipantData = loanParticipantData;
	}

	public void setLoanSettings(LoanSettings loanSettings) {
		this.loanSettings = loanSettings;
	}

	public LoanParticipantData getLoanParticipantData() {
		if (loanParticipantData == null) {
			loanParticipantData = dataRetriever.getLoanParticipantData(
					contractId, participantProfileId);
		}
		return loanParticipantData;
	}

	public LoanPlanData getLoanPlanData() {
		if (loanPlanData == null) {
			loanPlanData = dataRetriever.getLoanPlanData(contractId);
		}
		return loanPlanData;
	}

	public LoanSettings getLoanSettings() {
		if (loanSettings == null) {
			loanSettings = dataRetriever.getLoanSettings(contractId);
		}
		return loanSettings;
	}


	public boolean isIgnoreWarning() {
		return ignoreWarning;
	}

	public void setIgnoreWarning(boolean ignoreWarning) {
		this.ignoreWarning = ignoreWarning;
	}

	/**
	 * Retrieves the latest loan parameter.
	 *
	 * @return
	 */
	public LoanParameter getCurrentLoanParameter() {
		if (acceptedParameter != null) {
			return acceptedParameter;
		}
		if (reviewedParameter != null) {
			return reviewedParameter;
		}
		if (originalParameter != null) {
			return originalParameter;
		}
		return null;
	}

	public boolean isExternalUserInitiated() {
		return !LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(createdByRoleCode);
	}

	public boolean isInternalUserInitiated() {
		return LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE
				.equals(createdByRoleCode) ;
	}

	public boolean isParticipantInitiated() {
		return LoanConstants.USER_ROLE_PARTICIPANT_CODE
				.equals(createdByRoleCode);
	}

	public boolean isParticipantNotInitiated() {
		return isExternalUserInitiated() || isInternalUserInitiated();
	}

	/**
	 * Calculates the current available account balance, ignoring the excluded
	 * indicator.
	 *
	 * @return
	 */
	public BigDecimal getCurrentAccountBalance() {
		BigDecimal accountBalance = BigDecimal.ZERO;
		for (LoanMoneyType loanMoneyType : moneyTypes) {
			accountBalance = accountBalance.add(loanMoneyType
					.getAccountBalance());
		}
		return accountBalance.setScale(LoanDefaults.getLoanCalculationScale(),
				BigDecimal.ROUND_HALF_DOWN);
	}

	/**
	 * Calculates the current available account balance, taking into
	 * consideration the excluded indicator and the vesting percentage.
	 *
	 * @return
	 */
	public BigDecimal getCurrentAvailableAccountBalance() {
		BigDecimal accountBalance = BigDecimal.ZERO;
		BigDecimal moneyTypeBalance;
		for (LoanMoneyType loanMoneyType : moneyTypes) {
			if (!loanMoneyType.getExcludeIndicator()) {
				moneyTypeBalance = BigDecimal.ZERO;
				moneyTypeBalance = moneyTypeBalance.add(loanMoneyType
						.getAccountBalance());
				if (loanMoneyType.getVestingPercentage() != null) {
					moneyTypeBalance = moneyTypeBalance.multiply(loanMoneyType
							.getVestingPercentage());
					moneyTypeBalance = moneyTypeBalance.divide(
							GlobalConstants.ONE_HUNDRED,
							LoanConstants.AMOUNT_SCALE, RoundingMode.HALF_UP);
				}
				accountBalance = accountBalance.add(moneyTypeBalance);
			}
		}
		return accountBalance.setScale(LoanDefaults.getLoanCalculationScale(),
				BigDecimal.ROUND_HALF_DOWN);
	}

	public Fee getFee() {
		return fee;
	}

	public void setFee(Fee fee) {
		this.fee = fee;
	}

	public Loan() {
		setStatus(LoanStateEnum.DRAFT.getStatusCode());
	}

	public String getDefaultProvision() {
		return defaultProvision;
	}

	public void setDefaultProvision(String defaultProvision) {
		this.defaultProvision = defaultProvision;
	}

	public Integer getLoginUserProfileId() {
		return loginUserProfileId;
	}

	public void setLoginUserProfileId(Integer userProfileId) {
		this.loginUserProfileId = userProfileId;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Integer createdId) {
		this.createdId = createdId;
	}

	public Integer getLastUpdatedId() {
		return lastUpdatedId;
	}

	public void setLastUpdatedId(Integer lastUpdatedId) {
		this.lastUpdatedId = lastUpdatedId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}

	public LoanParameter getAcceptedParameter() {
		return acceptedParameter;
	}

	public void setAcceptedParameter(LoanParameter acceptedParameter) {
		this.acceptedParameter = acceptedParameter;
	}

	public BigDecimal getCurrentOutstandingBalance() {
		return currentOutstandingBalance;
	}

	public void setCurrentOutstandingBalance(
			BigDecimal currentOutstandingBalance) {
		this.currentOutstandingBalance = currentOutstandingBalance;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getFirstPayrollDate() {
		return firstPayrollDate;
	}

	public void setFirstPayrollDate(Date firstPayrollDate) {
		this.firstPayrollDate = firstPayrollDate;
	}

	public Boolean getLegallyMarriedInd() {
		return legallyMarriedInd;
	}

	public void setLegallyMarriedInd(Boolean legallyMarriedIn) {
		this.legallyMarriedInd = legallyMarriedIn;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanReason() {
		return loanReason;
	}

	public void setLoanReason(String loanReason) {
		this.loanReason = loanReason;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public BigDecimal getMaxBalanceLast12Months() {
		return maxBalanceLast12Months;
	}

	public void setMaxBalanceLast12Months(BigDecimal maxBalanceLast12Months) {
		this.maxBalanceLast12Months = maxBalanceLast12Months;
	}

	public Integer getMaximumAmortizationYears() {
		return maximumAmortizationYears;
	}

	public void setMaximumAmortizationYears(Integer maximumAmortizationYears) {
		this.maximumAmortizationYears = maximumAmortizationYears;
	}

	public List<LoanMoneyType> getMoneyTypesWithAccountBalance() {
		List<LoanMoneyType> result = new ArrayList<LoanMoneyType>();
		for (LoanMoneyType moneyType : this.moneyTypes) {
			if (moneyType.getAccountBalance() != null
					&& moneyType.getAccountBalance().compareTo(BigDecimal.ZERO) > 0) {
				result.add(moneyType);
			}
		}
		return result;
	}

	public List<LoanMoneyType> getMoneyTypesWithoutAccountBalance() {
		List<LoanMoneyType> result = new ArrayList<LoanMoneyType>();
		for (LoanMoneyType moneyType : this.moneyTypes) {
			if (moneyType.getAccountBalance() != null
					&& moneyType.getAccountBalance().compareTo(BigDecimal.ZERO) <= 0) {
				result.add(moneyType);
			}
		}
		return result;
	}

	public List<LoanMoneyType> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<LoanMoneyType> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public LoanParameter getOriginalParameter() {
		return originalParameter;
	}

	public void setOriginalParameter(LoanParameter originalParameter) {
		this.originalParameter = originalParameter;
	}

	public Integer getOutstandingLoansCount() {
		return outstandingLoansCount;
	}

	public void setOutstandingLoansCount(Integer outstandingLoansCount) {
		this.outstandingLoansCount = outstandingLoansCount;
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public Integer getParticipantProfileId() {
		return participantProfileId;
	}

	public void setParticipantProfileId(Integer profileId) {
		this.participantProfileId = profileId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public LoanParameter getReviewedParameter() {
		return reviewedParameter;
	}

	public void setReviewedParameter(LoanParameter reviewedParameter) {
		this.reviewedParameter = reviewedParameter;
	}

	public String getCreatedByRoleCode() {
		return createdByRoleCode;
	}

	public void setCreatedByRoleCode(String createdRoleCode) {
		this.createdByRoleCode = createdRoleCode;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getLastFeeChangedByTpaProfileId() {
		return lastFeeChangedByTpaProfileId;
	}

	public void setLastFeeChangedByTpaProfileId(
			Integer lastFeeChangedByTpaProfileId) {
		this.lastFeeChangedByTpaProfileId = lastFeeChangedByTpaProfileId;
	}

	public Boolean getLastFeeChangedWasPlanSponsorUserInd() {
		return lastFeeChangedWasPlanSponsorUserInd;
	}

	public void setLastFeeChangedWasPlanSponsorUserInd(
			Boolean lastFeeChangedWasPlanSponsorUserInd) {
		this.lastFeeChangedWasPlanSponsorUserInd = lastFeeChangedWasPlanSponsorUserInd;
	}

	public LoanRecipient getRecipient() {
		return recipient;
	}

	public void setRecipient(LoanRecipient recipient) {
		this.recipient = recipient;
	}

	public List<LoanDeclaration> getDeclarations() {
		return declarations;
	}

	public void setDeclarations(List<LoanDeclaration> declarations) {
		this.declarations = declarations;
	}

	public LoanDeclaration getDeclaration(String typeCode) {
		for (LoanDeclaration declaration : this.declarations) {
			if (typeCode.equals(declaration.getTypeCode())) {
				return declaration;
			}
		}
		return null;
	}

	public boolean isTruthInLendingNoticeDeclarationExists() {
		return isDeclarationExists(Declaration.TRUTH_IN_LENDING_NOTICE);
	}

	public boolean isPromissoryNoteDeclarationExists() {
		return isDeclarationExists(Declaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE);
	}

	public boolean isParticipantDeclarationExists() {
		return isDeclarationExists(Declaration.LOAN_PARTICIPANT_AUTHORIZATION);
	}

	public boolean isAtRiskTransactionDeclarationExists() {
		return isDeclarationExists(Declaration.AT_RISK_TRANSACTION_TYPE_CODE);
	}

	/**
	 * Returns true if a declaration exists for the typeCode specified.
	 *
	 * @param typeCode
	 * @return
	 */
	public boolean isDeclarationExists(String typeCode) {
		if (typeCode == null) {
			return false;
		}
		for (LoanDeclaration declaration : this.declarations) {
			if (typeCode.equals(declaration.getTypeCode())) {
				return true;
			}
		}
		return false;
	}

	public List<ManagedContent> getManagedContents() {
		return managedContents;
	}

	public void setManagedContents(List<ManagedContent> managedContents) {
		this.managedContents = managedContents;
	}

	public List<LoanNote> getPreviousAdministratorNotes() {
		return previousAdministratorNotes;
	}

	public void setPreviousAdministratorNotes(
			List<LoanNote> previousAdministratorNotes) {
		this.previousAdministratorNotes = previousAdministratorNotes;
	}

	public List<LoanNote> getPreviousParticipantNotes() {
		return previousParticipantNotes;
	}

	public void setPreviousParticipantNotes(
			List<LoanNote> previousParticipantNotes) {
		this.previousParticipantNotes = previousParticipantNotes;
	}

	public LoanNote getCurrentAdministratorNote() {
		return currentAdministratorNote;
	}

	public void setCurrentAdministratorNote(LoanNote currentAdministratorNote) {
		this.currentAdministratorNote = currentAdministratorNote;
	}

	public LoanNote getCurrentParticipantNote() {
		return currentParticipantNote;
	}

	public void setCurrentParticipantNote(LoanNote currentParticipantNote) {
		this.currentParticipantNote = currentParticipantNote;
	}

	public boolean isStatusChange() {
		return !this.status.equals(this.previousStatus);
	}

	public boolean isOK() {
		if (!ignoreWarning) {
			return errors.size() == 0;
		}
		/*
		 * If ignore warning is true, we try to find if there is any error in
		 * the list.
		 */
		for (LoanMessage message : errors) {
			if (message instanceof LoanError) {
				return false;
			}
		}
		return true;
	}

	public List<LoanMessage> getErrors() {
		if (!ignoreWarning) {
			return errors;
		}

		/*
		 * If ignore warning, we remove the warnings from the list.
		 */
		List<LoanMessage> result = new ArrayList<LoanMessage>();
		for (LoanMessage message : errors) {
			if (message instanceof LoanError) {
				result.add(message);
			}
		}

		return result;
	}

	public void setErrors(List<LoanMessage> errors) {
		this.errors = errors;
	}

	public List<LoanMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<LoanMessage> messages) {
		this.messages = messages;
	}

	public EmployeeVestingInformation getEmployeeVestingInformation() {
		return employeeVestingInformation;
	}

	public void setEmployeeVestingInformation(
			EmployeeVestingInformation employeeVestingInformation) {
		this.employeeVestingInformation = employeeVestingInformation;
	}

	public BigDecimal getMaximumLoanAmount() {
		return maximumLoanAmount;
	}

	public void setMaximumLoanAmount(BigDecimal maximumLoanAmount) {
		this.maximumLoanAmount = maximumLoanAmount;
	}

	public BigDecimal getMinimumLoanAmount() {
		return minimumLoanAmount;
	}

	public void setMinimumLoanAmount(BigDecimal minimumLoanAmount) {
		this.minimumLoanAmount = minimumLoanAmount;
	}

	public BigDecimal getMaximumLoanPercentage() {
		return maximumLoanPercentage;
	}

	public void setMaximumLoanPercentage(BigDecimal maximumLoanPercentage) {
		this.maximumLoanPercentage = maximumLoanPercentage;
	}

	public String getSpousalConsentReqdInd() {
		return spousalConsentReqdInd;
	}

	public void setSpousalConsentReqdInd(String spousalConsentReqdInd) {
		this.spousalConsentReqdInd = spousalConsentReqdInd;
	}

	public BigDecimal getContractLoanMonthlyFlatFee() {
		return contractLoanMonthlyFlatFee;
	}

	public void setContractLoanMonthlyFlatFee(
			BigDecimal contractLoanMonthlyFlatFee) {
		this.contractLoanMonthlyFlatFee = contractLoanMonthlyFlatFee;
	}

	public BigDecimal getContractLoanExpenseMarginPct() {
		return contractLoanExpenseMarginPct;
	}

	public void setContractLoanExpenseMarginPct(
			BigDecimal contractLoanExpenseMarginPct) {
		this.contractLoanExpenseMarginPct = contractLoanExpenseMarginPct;
	}

	public String getAtRiskInd() {
		return atRiskInd;
	}

	public void setAtRiskInd(String atRiskInd) {
		this.atRiskInd = atRiskInd;
	}

	/**
	 * @return the atRiskAddress
	 */
	public Address getAtRiskAddress() {
		return atRiskAddress;
	}

	/**
	 * @param atRiskAddress the atRiskAddress to set
	 */
	public void setAtRiskAddress(Address atRiskAddress) {
		this.atRiskAddress = atRiskAddress;
	}

	public ManagedContent getManagedContent(String typeCode) {
		for (ManagedContent content : managedContents) {
			if (typeCode.equals(content.getContentTypeCode())) {
				return content;
			}
		}
		return null;
	}

	public String getLoginRoleCode() {
		return loginRoleCode;
	}

	public void setLoginRoleCode(String loginRoleCode) {
		this.loginRoleCode = loginRoleCode;
	}

	public boolean isLoginUserPlanSponsorOrTpa() {
		if (LoanConstants.USER_ROLE_PLAN_SPONSOR_CODE
				.equals(getLoginRoleCode())
				|| LoanConstants.USER_ROLE_TPA_CODE.equals(getLoginRoleCode())
				|| LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE.equals(getLoginRoleCode())) {
            // BGA Project 2012 - Modified for distinguishing loan/withdrawal entries
        	// Submitted by JH staff (Bundled GA CAR).  Submission DB code is "JH"
			return true;
		} else {
			return false;
		}
	}

	public boolean isFeeChanged() {
		return isFeeChanged;
	}

	public void setFeeChanged(boolean isFeeChanged) {
		this.isFeeChanged = isFeeChanged;
	}

    public Date getEffectiveDateOriginalDBValue() {
        return effectiveDateOriginalDBValue;
    }

    public void setEffectiveDateOriginalDBValue(Date newEstimatedEffectiveDate) {
        this.effectiveDateOriginalDBValue = newEstimatedEffectiveDate;
    }

    public Date getMaturityDateOriginalDBValue() {
        return maturityDateOriginalDBValue;
    }

    public void setMaturityDateOriginalDBValue(Date maturityDateOriginalDBValue) {
        this.maturityDateOriginalDBValue = maturityDateOriginalDBValue;
    }

    public boolean isAnyMoneyTypeNotAContractMoneyType() {
        for (LoanMoneyType moneyType : this.getMoneyTypes()) {
            if (!moneyType.isAContractMoneyType()) {
                return true;
            }
        }
        return false;
    }

	public Boolean getApplyIrs10KDollarRuleInd() {
		return applyIrs10KDollarRuleInd;
	}

	public void setApplyIrs10KDollarRuleInd(Boolean applyIrs10KDollarRuleInd) {
		this.applyIrs10KDollarRuleInd = applyIrs10KDollarRuleInd;
	}


	/**
	 * @return EmailChangePinAtRiskDays business parameter value
	 */
	public String getEmailChangePinAtRiskDays() {
		return emailChangePinAtRiskDays;
	}

	/**
	 * @param emailChangePinAtRiskDays
	 */
	public void setEmailChangePinAtRiskDays(String emailChangePinAtRiskDays) {
		this.emailChangePinAtRiskDays = emailChangePinAtRiskDays;
	}

	public boolean isBundledContract() {
		return isBundledContract;
	}

	/**
	 * @return the isSigningAuthorityForContractTpaFirm
	 */
	public boolean isSigningAuthorityForContractTpaFirm() {
		return isSigningAuthorityForContractTpaFirm;
	}

	/**
	 * @param isSigningAuthorityForContractTpaFirm the isSigningAuthorityForContractTpaFirm to set
	 */
	public void setSigningAuthorityForContractTpaFirm(
			boolean isSigningAuthorityForContractTpaFirm) {
		this.isSigningAuthorityForContractTpaFirm = isSigningAuthorityForContractTpaFirm;
	}

	/**
	 * @param isBundledContract the isBundledContract to set
	 */
	public void setBundledContract(boolean isBundledContract) {
		this.isBundledContract = isBundledContract;
	}

	public boolean isDeclartionSectionDisplayed() {
		return declartionSectionDisplayed;
	}

	public void setDeclartionSectionDisplayed(
			boolean isPromissoryNoteAndTLNoticeSelectable) {
		this.declartionSectionDisplayed = isPromissoryNoteAndTLNoticeSelectable;
	}

	/**
	 * @return the atRiskDetailsVO
	 */
	public AtRiskDetailsVO getAtRiskDetailsVO() {
		return atRiskDetailsVO;
	}

	/**
	 * @param atRiskDetailsVO the atRiskDetailsVO to set
	 */
	public void setAtRiskDetailsVO(AtRiskDetailsVO atRiskDetailsVO) {
		this.atRiskDetailsVO = atRiskDetailsVO;
	}

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


}
