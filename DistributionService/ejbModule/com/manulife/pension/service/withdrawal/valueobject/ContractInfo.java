package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.UnvestedMoneyOption;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.employee.valueobject.BasicAddressVO;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.loan.valueobject.LoanSettings;

/**
 * ContractInfo contains the status of the contract service features for the withdrawals project.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/12/12 15:58:08
 */
public class ContractInfo extends BaseSerializableCloneableObject {

    private static final Logger logger = Logger.getLogger(ContractInfo.class);

    /**
     * Contract status code for active contracts.
     */
    public static final String CONTRACT_STATUS_ACTIVE = Contract.STATUS_ACTIVE_CONTRACT;

    /**
     * Contract status code for frozen contracts.
     */
    public static final String CONTRACT_STATUS_FROZEN = Contract.STATUS_CONTRACT_FROZEN;

    /**
     * Contract status code for discontinued contracts.
     */
    public static final String CONTRACT_STATUS_DISCONTINUED = Contract.STATUS_CONTRACT_DISCONTINUED;

    /**
     * Contract status code for discontinued contracts.
     */
    public static final String CONTRACT_STATUS_PROPOSAL_SIGNED = Contract.STATUS_PROPOSAL_SIGNED;

    /**
     * Contract status code for discontinued contracts.
     */
    public static final String CONTRACT_STATUS_DETAILS_COMPLETED = Contract.STATUS_DETAILS_COMPLETED;

    /**
     * Contract status code for discontinued contracts.
     */
    public static final String CONTRACT_STATUS_PENDING_CONTRACT_APPROVAL = Contract.STATUS_PENDING_CONTRACT_APPROVAL;

    /**
     * Contract status code for approved contracts.
     */
    public static final String CONTRACT_STATUS_CONTRACT_APPROVED = Contract.STATUS_CONTRACT_APPROVED;

    /**
     * Contract status code for other contracts.
     */
    public static final String CONTRACT_STATUS_OTHER = Contract.STATUS_OTHER;

    public static final String CHECK_MAILED_TO_CODE_PLAN_ADMINISTRATOR = "PA";

    public static final String CHECK_MAILED_TO_CODE_PAYEE = "PY";

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private static final String YES = "Y";

    private Integer contractId;

    private Boolean planContractServiceFeatureEnabled;

    private Boolean mailChequeToAddressIndicator;

    private Boolean hasATpaFirm;

    private Boolean definedBenefits;

    private String clientAccountRepId;

    private String clientAccountRepEmail;

    private String teamCode;

    private Boolean twoStepApprovalRequired = false;

    // Contract Service Feature: Allow Online W/Ds
    private Boolean csfAllowOnlineWithdrawals;

    // Contract Service Feature: Allow Online Loans
    private Boolean csfAllowOnlineLoans;

    // Contract Service Feature: use IRS Special Tax
    private Boolean csfIRSSpecialTaxNotice;

    private Boolean statusIsAcOrCf;

    // Withdrawal Permissions
    private Boolean hasInitiatePermission;

    private Boolean hasReviewPermission;

    private Boolean hasApprovePermission;

    private Boolean hasViewAllPermission;

    // loan permissions
    private Boolean hasViewAllLoansPermission;

    private Boolean hasIntitiateLoanPermission;

    private Boolean hasReviewLoanPermission;

    private Boolean hasSelectedAccessPermission;

    private String product;

    private String status;

    // Contract Service Feature
    private Boolean initiatorMayApprove;

    private Boolean initiatorMayApproveLoans;

    private Boolean contractHasRothEnabled;

    private Boolean contractHasPbaEnabled;

    // Other Contract Service Details

    private Boolean spousalConsentRequired;

    private Address trusteeAddress;

    private Boolean earlyRetirementAllowed;

    private BigDecimal earlyAgeRetirement;

    private BigDecimal normalRetirementAge;

    private String defaultUnvestedMoneyOptionCode;

    private String effectiveDefaultUnvestedMoneyOptionCode;

    private Collection<DeCodeVO> unvestedMoneyOptions;

    private Collection<WithdrawalReason> withdrawalReasons;

    private Collection<String> fullyVestedWithdrawalReasons;

    private boolean tpaStaffPlanIndicator;

	private boolean isBundledGaIndicator = false;

    /**
     * Default Constructor.
     */
    public ContractInfo() {
        super();
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(final Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the hasATpaFirm
     */
    public Boolean getHasATpaFirm() {
        return hasATpaFirm;
    }

    /**
     * @param hasATpaFirm the hasATpaFirm to set
     */
    public void setHasATpaFirm(final Boolean hasATpaFirm) {
        this.hasATpaFirm = hasATpaFirm;
    }

    /**
     * @return the mailChequeToAddressIndicator
     */
    public Boolean getMailChequeToAddressIndicator() {
        return mailChequeToAddressIndicator;
    }

    /**
     * @param mailChequeToAddressIndicator the mailChequeToAddressIndicator to set
     */
    public void setMailChequeToAddressIndicator(final Boolean mailChequeToAddressIndicator) {
        this.mailChequeToAddressIndicator = mailChequeToAddressIndicator;
    }

    /**
     * @return the planContractServiceFeatureEnabled
     */
    public Boolean getPlanContractServiceFeatureEnabled() {
        return planContractServiceFeatureEnabled;
    }

    /**
     * @param planContractServiceFeatureEnabled the planContractServiceFeatureEnabled to set
     */
    public void setPlanContractServiceFeatureEnabled(final Boolean planContractServiceFeatureEnabled) {
        this.planContractServiceFeatureEnabled = planContractServiceFeatureEnabled;
    }

    /**
     * @return String - The clientAccountRepId.
     */
    public String getClientAccountRepId() {
        return clientAccountRepId;
    }

    /**
     * @param clientAccountRepId - The clientAccountRepId to set.
     */
    public void setClientAccountRepId(final String clientAccountRepId) {
        this.clientAccountRepId = clientAccountRepId;
    }

    /**
     * @return String - The teamCode.
     */
    public String getTeamCode() {
        return teamCode;
    }

    /**
     * @param teamCode - The teamCode to set.
     */
    public void setTeamCode(final String teamCode) {
        this.teamCode = teamCode;
    }

    /**
     * @return the twoStepApprovalRequired
     */
    public Boolean getTwoStepApprovalRequired() {
        return twoStepApprovalRequired;
    }

    /**
     * @param twoStepApprovalRequired the twoStepApprovalRequired to set
     */
    public void setTwoStepApprovalRequired(final Boolean twoStepApprovalRequired) {
        this.twoStepApprovalRequired = twoStepApprovalRequired;
    }

    /**
     * Gets the value of the Allow Online W/Ds CSF.
     * 
     * @return TRUE if Online W/Ds are allowed
     */
    public Boolean getCsfAllowOnlineWithdrawals() {
        return csfAllowOnlineWithdrawals;
    }

    /**
     * Sets the value of the Allow Online W/Ds CSF.
     * 
     * @param csfAllowOnlineWithdrawals Value of Online W/Ds CSF
     */
    public void setCsfAllowOnlineWithdrawals(final Boolean csfAllowOnlineWithdrawals) {
        this.csfAllowOnlineWithdrawals = csfAllowOnlineWithdrawals;
    }

    /**
     * Gets the value of the Allow Online Loans CSF.
     * 
     * @return TRUE if Online Loans are allowed
     */
    public Boolean getCsfAllowOnlineLoans() {
        return csfAllowOnlineLoans;
    }

    /**
     * Sets the value of the Allow Online Loans CSF.
     * 
     * @param csfAllowOnlineLoans Value of Online Loans CSF
     */
    public void setCsfAllowOnlineLoans(final Boolean csfAllowOnlineLoans) {
        this.csfAllowOnlineLoans = csfAllowOnlineLoans;
    }

    /**
     * Gets the value of the AC or CF status.
     * 
     * @return TRUE if contract status is AC or CF
     */
    public Boolean getStatusIsAcOrCf() {
        return statusIsAcOrCf;
    }

    /**
     * Sets the value of the AC or CF status.
     * 
     * @param statusIsAcOrCf Value of AC or CF status
     */
    public void setStatusIsAcOrCf(final Boolean statusIsAcOrCf) {
        this.statusIsAcOrCf = statusIsAcOrCf;
    }

    /**
     * @return the hasInitiatePermission
     */
    public Boolean getHasInitiatePermission() {
        return hasInitiatePermission;
    }

    /**
     * @param hasInitiatePermission the hasInitiatePermission to set
     */
    public void setHasInitiatePermission(final Boolean hasInitiatePermission) {
        this.hasInitiatePermission = hasInitiatePermission;
    }

    /**
     * @return the hasReviewPermission
     */
    public Boolean getHasReviewPermission() {
        return hasReviewPermission;
    }

    /**
     * @param hasReviewPermission the hasReviewPermission to set
     */
    public void setHasReviewPermission(final Boolean hasReviewPermission) {
        this.hasReviewPermission = hasReviewPermission;
    }

    /**
     * @return the hasApprovePermission
     */
    public Boolean getHasApprovePermission() {
        return hasApprovePermission;
    }

    /**
     * @param hasApprovePermission the hasApprovePermission to set
     */
    public void setHasApprovePermission(final Boolean hasApprovePermission) {
        this.hasApprovePermission = hasApprovePermission;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(final String product) {
        this.product = product;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return the initiatorMayApprove
     */
    public Boolean getInitiatorMayApprove() {
        return initiatorMayApprove;
    }

    /**
     * @param initiatorMayApprove the initiatorMayApprove to set
     */
    public void setInitiatorMayApprove(final Boolean initiatorMayApprove) {
        this.initiatorMayApprove = initiatorMayApprove;
    }

    /**
     * Queries if the create withdrawal request link should be shown or suppressed.
     * 
     * @return boolean - True if the link should be shown.
     */
    public boolean getShowCreateWithdrawalRequestLink() {

        // Suppress if user does not have initiate permission
        if (BooleanUtils.isNotTrue(getHasInitiatePermission())) {
            return false;
        }

        // Suppress if contract does not have contract service feature for online withdrawals
        if (BooleanUtils.isNotTrue(getCsfAllowOnlineWithdrawals())) {
            return false;
        }

        // Suppress if contract has defined benefits
        if (BooleanUtils.isNotFalse(getDefinedBenefits())) {
            return false;
        }

        // Suppress if contract status is not active or frozen
        if (StringUtils.equals(getStatus(), CONTRACT_STATUS_ACTIVE)
                || StringUtils.equals(getStatus(), CONTRACT_STATUS_FROZEN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Queries if the withdrawal or loans tools link should be shown or suppressed.
     * 
     * @return boolean - True if the link should be shown.
     */
    // TODO change the method name which is applicable for both loans and W/Ds.
    public boolean getShowWithdrawalToolsLink() {

        // Suppress if user does not have view all permission
        if (BooleanUtils.isNotTrue(getHasViewAllPermission())
                && BooleanUtils.isNotTrue(getHasViewAllLoansPermission())) {
            return false;
        }

        // Suppress if user has selected access permission
        if (BooleanUtils.isTrue(getHasSelectedAccessPermission())) {
            return false;
        }

        // Suppress if contract does not have contract service feature for online withdrawals or
        // for online loans
        if (BooleanUtils.isNotTrue(getCsfAllowOnlineWithdrawals())
                && BooleanUtils.isNotTrue(getCsfAllowOnlineLoans())) {
            return false;
        }

        // Suppress if contract has defined benefits
        if (BooleanUtils.isNotFalse(getDefinedBenefits())) {
            return false;
        }

        // Suppress if contract status is not active or frozen
        if (StringUtils.equals(getStatus(), CONTRACT_STATUS_ACTIVE)
                || StringUtils.equals(getStatus(), CONTRACT_STATUS_FROZEN)
                || StringUtils.equals(getStatus(), CONTRACT_STATUS_DISCONTINUED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the definedBenefits
     */
    public Boolean getDefinedBenefits() {
        return definedBenefits;
    }

    /**
     * @param definedBenefits the definedBenefits to set
     */
    public void setDefinedBenefits(final Boolean definedBenefits) {
        this.definedBenefits = definedBenefits;
    }

    /**
     * @return the hasViewAllLoansPermission
     */
    public Boolean getHasViewAllLoansPermission() {
        return hasViewAllLoansPermission;
    }

    /**
     * @param hasViewAllLoansPermission the hasViewAllLoansPermission to set
     */
    public void setHasViewAllLoansPermission(final Boolean hasViewAllLoansPermission) {
        this.hasViewAllLoansPermission = hasViewAllLoansPermission;
    }

    /**
     * @return the hasIntitiateLoanPermission
     */
    public Boolean getHasIntitiateLoanPermission() {
        return hasIntitiateLoanPermission;
    }

    /**
     * @param hasIntitiateLoanPermission the hasIntitiateLoanPermission to set
     */
    public void setHasIntitiateLoanPermission(final Boolean hasIntitiateLoanPermission) {
        this.hasIntitiateLoanPermission = hasIntitiateLoanPermission;
    }

    /**
     * @return the hasReviewLoanPermission
     */
    public Boolean getHasReviewLoanPermission() {
        return hasReviewLoanPermission;
    }

    /**
     * @param hasReviewLoanPermission the hasReviewLoanPermission to set
     */
    public void setHasReviewLoanPermission(final Boolean hasReviewLoanPermission) {
        this.hasReviewLoanPermission = hasReviewLoanPermission;
    }

    /**
     * @return the hasViewAllPermission
     */
    public Boolean getHasViewAllPermission() {
        return hasViewAllPermission;
    }

    /**
     * @param hasViewAllPermission the hasViewAllPermission to set
     */
    public void setHasViewAllPermission(final Boolean hasViewAllPermission) {
        this.hasViewAllPermission = hasViewAllPermission;
    }

    /**
     * @return the hasSelectedAccessPermission
     */
    public Boolean getHasSelectedAccessPermission() {
        return hasSelectedAccessPermission;
    }

    /**
     * @param hasSelectedAccessPermission the hasSelectedAccessPermission to set
     */
    public void setHasSelectedAccessPermission(final Boolean hasSelectedAccessPermission) {
        this.hasSelectedAccessPermission = hasSelectedAccessPermission;
    }

    /**
     * @return the contractHasRothEnabled
     */
    public Boolean getContractHasRothEnabled() {
        return contractHasRothEnabled;
    }

    /**
     * @param contractHasRothEnabled the contractHasRothEnabled to set
     */
    public void setContractHasRothEnabled(final Boolean contractHasRothEnabled) {
        this.contractHasRothEnabled = contractHasRothEnabled;
    }

    /**
     * @return the contractHasPbaEnabled
     */
    public Boolean getContractHasPbaEnabled() {
        return contractHasPbaEnabled;
    }

    /**
     * @param contractHasPbaEnabled the contractHasPbaEnabled to set
     */
    public void setContractHasPbaEnabled(final Boolean contractHasPbaEnabled) {
        this.contractHasPbaEnabled = contractHasPbaEnabled;
    }

    public BigDecimal getEarlyAgeRetirement() {
        return earlyAgeRetirement;
    }

    public void setEarlyAgeRetirement(final BigDecimal earlyAgeRetirement) {
        this.earlyAgeRetirement = earlyAgeRetirement;
    }

    public Boolean getEarlyRetirementAllowed() {
        return earlyRetirementAllowed;
    }

    public void setEarlyRetirementAllowed(final Boolean earlyRetirementAllowed) {
        this.earlyRetirementAllowed = earlyRetirementAllowed;
    }

    public BigDecimal getNormalRetirementAge() {
        return normalRetirementAge;
    }

    public void setNormalRetirementAge(final BigDecimal normalRetirementAge) {
        this.normalRetirementAge = normalRetirementAge;
    }

    public Boolean getSpousalConsentRequired() {
        return spousalConsentRequired;
    }

    public void setSpousalConsentRequired(final Boolean spousalContentRequired) {
        this.spousalConsentRequired = spousalContentRequired;
    }

    public Address getTrusteeAddress() {
        return trusteeAddress;
    }

    public void setTrusteeAddress(final BasicAddressVO trusteeAddress) {
        this.trusteeAddress = convertTrusteeAddress(trusteeAddress);
    }

    public Collection<DeCodeVO> getUnvestedMoneyOptions() {
        return unvestedMoneyOptions;
    }

    public void setUnvestedMoneyOptions(final Collection<UnvestedMoneyOption> unvestedMoneyOptions) {
        this.unvestedMoneyOptions = convertUnvestedMoneyOptions(unvestedMoneyOptions);
    }

    public Collection<WithdrawalReason> getWithdrawalReasons() {
        return withdrawalReasons;
    }

    public void setWithdrawalReasons(final Collection<WithdrawalReason> withdrawalReasons) {
        this.withdrawalReasons = withdrawalReasons;
    }

    public String getDefaultUnvestedMoneyOptionCode() {
        return defaultUnvestedMoneyOptionCode;
    }

    public void setDefaultUnvestedMoneyOptionCode(String defaultUnvestedMoneyOptionCode) {
        this.defaultUnvestedMoneyOptionCode = defaultUnvestedMoneyOptionCode;
    }

    /**
     * Converts the {@link UnvestedMoneyOption} objects in the provided collection into
     * {@link DeCodeVO} objects that are used for display as well as setting the default unvested
     * money option from the list.
     * 
     * @param unvestedMoneyOptions The collection of unvested money options from plan data.
     * @return Collection - A Collection of DeCodeVO objects created from the given list.
     */
    private Collection<DeCodeVO> convertUnvestedMoneyOptions(
            final Collection<UnvestedMoneyOption> unvestedMoneyOptions) {

        final Collection<DeCodeVO> unvestedOptions = new ArrayList<DeCodeVO>();
        for (UnvestedMoneyOption unvestedMoneyOption : unvestedMoneyOptions) {
            logger.info(new StringBuffer("convertUnvestedMoneyOptions> Examining plan option [")
                    .append(unvestedMoneyOption).append("].").toString());
            if (StringUtils.isNotBlank(unvestedMoneyOption.getOptionCode())
                    && StringUtils.isNotBlank(unvestedMoneyOption.getOptionDesc())) {
                DeCodeVO deCodeVo = new DeCodeVO(unvestedMoneyOption.getOptionCode(),
                        unvestedMoneyOption.getOptionDesc());
                if (StringUtils.equalsIgnoreCase(YES, unvestedMoneyOption.getDefaultInd())) {
                    defaultUnvestedMoneyOptionCode = unvestedMoneyOption.getOptionCode();
                }
                unvestedOptions.add(deCodeVo);
            }
        }

        return unvestedOptions;
    }

    private Address convertTrusteeAddress(BasicAddressVO basicAddress) {
        Address address = new Address();
        address.setAddressLine1(basicAddress.getAddressLine1());
        address.setAddressLine2(basicAddress.getAddressLine2());
        address.setCity(basicAddress.getCity());
        address.setCountryCode(basicAddress.getCountry());
        address.setZipCode(basicAddress.getZipCode());
        return address;
    }

    public String getClientAccountRepEmail() {
        return clientAccountRepEmail;
    }

    public void setClientAccountRepEmail(String clientAccountRepEmail) {
        this.clientAccountRepEmail = clientAccountRepEmail;
    }

    /**
     * @return the effectiveDefaultUnvestedMoneyOptionCode
     */
    public String getEffectiveDefaultUnvestedMoneyOptionCode() {
        return effectiveDefaultUnvestedMoneyOptionCode;
    }

    /**
     * @param effectiveDefaultUnvestedMoneyOptionCode the effectiveDefaultUnvestedMoneyOptionCode to
     *            set
     */
    public void setEffectiveDefaultUnvestedMoneyOptionCode(
            final String effectiveDefaultUnvestedMoneyOptionCode) {
        this.effectiveDefaultUnvestedMoneyOptionCode = effectiveDefaultUnvestedMoneyOptionCode;
    }

    public boolean getTpaStaffPlanIndicator() {
        return tpaStaffPlanIndicator;
    }

    public void setTpaStaffPlanIndicator(boolean contractStaffPlan) {
        this.tpaStaffPlanIndicator = contractStaffPlan;
    }

    /**
     * Queries if the create loan request link should be shown or suppressed.
     * 
     * @return boolean - True if the link should be shown.
     */
    public boolean getShowCreateLoanRequestLink() {

        // Suppress if user does not have initiate permission
        if (BooleanUtils.isNotTrue(getHasIntitiateLoanPermission())) {
            return false;
        }

        // Suppress if contract does not have contract service feature for online withdrawals
        if (BooleanUtils.isNotTrue(getCsfAllowOnlineLoans())) {
            return false;
        }

        // Suppress if contract has defined benefits
        if (BooleanUtils.isNotFalse(getDefinedBenefits())) {
            return false;
        }

        // Suppress if contract status is not active or frozen
        if (StringUtils.equals(getStatus(), CONTRACT_STATUS_ACTIVE)
                || StringUtils.equals(getStatus(), CONTRACT_STATUS_FROZEN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Queries if the create loan request link should be shown or suppressed.
     * 
     * @return boolean - True if the link should be shown.
     */
    public boolean getShowCreateLoanRequestLink(LoanSettings loanSettings) {

        // Suppress if Allow online loans = No and contract product feature = LRK01
        if (!(loanSettings.isAllowOnlineLoans()) && loanSettings.isLrk01()) {
            return false;
        }

        // Suppress if user does not have initiate permission
        if (BooleanUtils.isNotTrue(getHasIntitiateLoanPermission())) {
            return false;
        }

        // Suppress if contract has defined benefits
        if (BooleanUtils.isNotFalse(getDefinedBenefits())) {
            return false;
        }

        // Suppress if contract status is not active or frozen
        if (StringUtils.equals(getStatus(), CONTRACT_STATUS_ACTIVE)
                || StringUtils.equals(getStatus(), CONTRACT_STATUS_FROZEN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the initiatorMayApproveLoans
     */
    public Boolean getInitiatorMayApproveLoans() {
        return initiatorMayApproveLoans;
    }

    /**
     * @param initiatorMayApproveLoans the initiatorMayApproveLoans to set
     */
    public void setInitiatorMayApproveLoans(Boolean initiatorMayApproveLoans) {
        this.initiatorMayApproveLoans = initiatorMayApproveLoans;
    }

    /**
     * @return Collection<String> - The fullyVestedWithdrawalReasons.
     */
    public Collection<String> getFullyVestedWithdrawalReasons() {
        return fullyVestedWithdrawalReasons;
    }

    /**
     * @param fullyVestedWithdrawalReasons - The fullyVestedWithdrawalReasons to set.
     */
    public void setFullyVestedWithdrawalReasons(Collection<String> fullyVestedWithdrawalReasons) {
        this.fullyVestedWithdrawalReasons = fullyVestedWithdrawalReasons;
    }

    public Boolean getCsfIRSSpecialTaxNotice() {
        return csfIRSSpecialTaxNotice;
    }

    public void setCsfIRSSpecialTaxNotice(Boolean csfIRSSpecialTaxNotice) {
        this.csfIRSSpecialTaxNotice = csfIRSSpecialTaxNotice;
    }
    /**
     * Added for BGA Project Sept 2012
     * Set to "true" if the contract is a BGA (Bundled Group Annuity) contract
     * These contracts have John Hancock as the TPA, and have two special roles associated
     * with them BGA CAR and BGA Approver (for handling participant requests for loans and
     * withdrawals.   
     * 
     * @param bgaIndicator
     */
	public void setIsBundledGaIndicator(boolean bgaIndicator) {
		this.isBundledGaIndicator = bgaIndicator;		
	}
	
	/**
     * Added for BGA Project Sept 2012
     * This method tells you if the contract is a BGA (Bundled Group Annuity) contract
     * These contracts have John Hancock as the TPA, and have two special roles associated
     * with them BGA CAR and BGA Approver (for handling participant requests for loans and
     * withdrawals.   
     * 
     * @return true if the contract is a BGA contract.
     */
	public boolean isBundledGaIndicator() {
		return this.isBundledGaIndicator;
	}
}
