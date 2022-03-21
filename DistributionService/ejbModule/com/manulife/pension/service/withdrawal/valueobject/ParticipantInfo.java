package com.manulife.pension.service.withdrawal.valueobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * Value object to hold the results returned by the EZK100.GET_PARTICIPANT_WITHDRAWAL_INFO stored
 * proc. To be used in the Withdrawal Request pages.
 * 
 * @author Aurelian Penciu
 */
public class ParticipantInfo extends BaseSerializableCloneableObject {
    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Participant status code for partial termination of participation.
     */
    public static final String PARTICIPANT_STATUS_PARTIAL_TERMINATION_OF_PARTICIPATION = "AP";

    /**
     * Participant status code for partial disability.
     */
    public static final String PARTICIPANT_STATUS_PARTIAL_DISABILITY = "BP";

    /**
     * Participant status code for partial death.
     */
    public static final String PARTICIPANT_STATUS_PARTIAL_DEATH = "DP";

    /**
     * Participant status code for partial retirement.
     */
    public static final String PARTICIPANT_STATUS_PARTIAL_RETIREMENT = "RP";

    /**
     * Participant status code for partial termination.
     */
    public static final String PARTICIPANT_STATUS_PARTIAL_TERMINATION = "TP";

    /**
     * Participant status code for termination of participation paid up.
     */
    public static final String PARTICIPANT_STATUS_TERMINATION_OF_PARTICIPANT_PAID_UP = "AT";

    /**
     * Participant status code for disability total.
     */
    public static final String PARTICIPANT_STATUS_DISABILITY_TOTAL = "BT";

    /**
     * Participant status code for death total.
     */
    public static final String PARTICIPANT_STATUS_DEATH_TOTAL = "DT";

    /**
     * Participant status code for retired total.
     */
    public static final String PARTICIPANT_STATUS_RETIRED_TOTAL = "RT";

    /**
     * Participant status code for termination total.
     */
    public static final String PARTICIPANT_STATUS_TERMINATED_TOTAL = "TT";

    /**
     * Participant status code for termination via NE withdrawal (opt-out).
     */
    public static final String PARTICIPANT_STATUS_OPTED_OUT = "NT";

    /**
     * Check is payable to client.
     */
    public static final String CHECK_PAYABLE_TO_CLIENT = "CL";

    /**
     * Check is payable to participant.
     */
    public static final String CHECK_PAYABLE_TO_PARTICIPANT = "PR";

    /**
     * Check is payable to trustee.
     */
    public static final String CHECK_PAYABLE_TO_TRUSTEE = "TR";

    // TODO: document this field
    private Date lastContributionDate;

    // TODO: document this field
    private Date lastInvestmentDate;

    private Date systemOfRecordDateOfBirth;

    private Date participantEnrollmentDate;

    // This isn't US or NY, but the company ID (see CONTRACT_CS.MANULIFE_COMPANY_ID).
    private String manulifeCompanyId;

    /**
     * After Tax Contribution indicator.
     */
    private Boolean hasAfterTaxContributions;

    /**
     * Contract name. Value of the EZK100.CONTRACT_CS.CONTRACT_NAME field.
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=CONTRACT%5FCS
     */
    private String contractName;

    /**
     * Contract effective date. Value of the EZK100.CONTRACT_CS.EFFECTIVE_DATE field.
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=CONTRACT%5FCS
     */
    private Date contractEffectiveDate;

    /**
     * Value of the EZK100.CONTRACT_CS.CHECK_PAYABLE_TO_CODE field. Indicates the payee as follows:
     * CL = Client, PR = Participant, TR = Trustee
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=CONTRACT%5FCS
     */
    private String chequePayableToCode;

    /**
     * Concatenation of EZK100.CONTRACT_CS.CONTRACT_LONG_NAME1 and
     * EZK100.CONTRACT_CS.CONTRACT_LONG_NAME as the trustee name.
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=CONTRACT%5FCS
     */
    private String trusteeName;

    /**
     * MTA Contract indicator. Set to TRUE if the PSW100.CLIENT.GROUP_FIELD_OFFICE_NO field value is
     * '25270' or '25280'.
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=CLIENT
     */
    private boolean isMTAContract;

    /**
     * PBA Account indicator.
     */
    private Boolean participantHasPbaMoney;

    /**
     * Roth Money indicator.
     */
    private Boolean participantHasRothMoney;

//    /**
//     * The name of the Pension Plan.
//     * 
//     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=PENSION%5FPLAN
//     */
//    private String planName;
//
    /**
     * Flag that indicates whether the participant has an existing withdrawal request that has not
     * been processed.
     */
    private Boolean hasExistingWithdrawalRequest;
    /**
     * Participant status code.
     */
    private String participantStatusCode;

    /**
     * Contract status code.
     */
    private String contractStatusCode;

    /**
     * Collection of outstanding loan IDs and balances.
     */
    private Collection<WithdrawalRequestLoan> outstandingLoans;

    /**
     * List of available withdrawal amounts grouped by money type (excluding loans and PBAs).
     */
    private Collection<WithdrawalRequestMoneyType> moneyTypes;

    /**
     * THIRD_PARTY_ADMIN_ID for the contract exists.
     */
    private Boolean thirdPartyAdminId;

    /**
     * List of Money Type aliases used for name resolution NOTE: This is quasi-static info and
     * should be actually cached somewhere in the VM rather then retrieving over and over again...
     */
    private Map<String, String> moneyTypeAliases;

    /**
     * @return the thirdPartyAdminId
     */
    public Boolean getThirdPartyAdminId() {
        return thirdPartyAdminId;
    }

    /**
     * @param thirdPartyAdminId the thirdPartyAdminId to set
     */
    public void setThirdPartyAdminId(final Boolean thirdPartyAdminId) {
        this.thirdPartyAdminId = thirdPartyAdminId;
    }

    /**
     * Returns the value of the hasAfterTaxContributions indicator.
     * 
     * @return True if participant has After tax contributions
     */
    public Boolean getHasAfterTaxContributions() {
        return hasAfterTaxContributions;
    }

    /**
     * Sets the value of the hasAfterTaxContributions indicator.
     * 
     * @param hasAfterTaxContributions - True if participant has After tax contributions
     */
    public void setHasAfterTaxContributions(final Boolean hasAfterTaxContributions) {
        this.hasAfterTaxContributions = hasAfterTaxContributions;
    }

    /**
     * Returns the most recent contribution date.
     * 
     * @return Date Last contribution date.
     */
    public Date getLastContributionDate() {
        return lastContributionDate;
    }

    /**
     * Sets the most recent contribution date.
     * 
     * @param lastContributionDate Date Last contribution date.
     */
    public void setLastContributionDate(final Date lastContributionDate) {
        this.lastContributionDate = lastContributionDate;
    }

    /**
     * Returns the last applicable contribution date.
     * 
     * @return Date Last applicable contribution date.
     */
    public Date getLastInvestmentDate() {
        return lastInvestmentDate;
    }

    /**
     * Sets the last applicable contribution date.
     * 
     * @param lastInvestmentDate - Date Last applicable contribution date.
     */
    public void setLastInvestmentDate(final Date lastInvestmentDate) {
        this.lastInvestmentDate = lastInvestmentDate;
    }

    /**
     * Sets the contract effective date.
     * 
     * @return Contract effective date.
     */
    public Date getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    /**
     * Sets the contract effective date.
     * 
     * @param contractEffectiveDate - Date Contract effective date.
     */
    public void setContractEffectiveDate(final Date contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    /**
     * Returns the list of outstanding loans.
     * 
     * @return Collection List of outstanding loans.
     */
    public Collection<WithdrawalRequestLoan> getOutstandingLoans() {
        return outstandingLoans;
    }

    /**
     * Sets the list of outstanding loans.
     * 
     * @param outstandingLoans Collection List of outstanding loans.
     */
    public void setOutstandingLoans(final Collection<WithdrawalRequestLoan> outstandingLoans) {
        this.outstandingLoans = outstandingLoans;
    }

    /**
     * Returns the long Trustee (TPA) name.
     * 
     * @return String Trustee name
     */
    public String getTrusteeName() {
        return trusteeName;
    }

    /**
     * Sets the long Trustee (TPA) name.
     * 
     * @param trusteeName - String Trustee name
     */
    public void setTrusteeName(final String trusteeName) {
        this.trusteeName = trusteeName;
    }

    /**
     * Tests if the Contract is MTA.
     * 
     * @return true if the Contract is MTA, false otherwise.
     */
    public boolean getIsMTAContract() {
        return isMTAContract;
    }

    /**
     * Setss the Contract MTA flag.
     * 
     * @param isMTAContract - the Contract MTA flag
     */
    public void setIsMTAContract(final boolean isMTAContract) {
        this.isMTAContract = isMTAContract;
    }

    /**
     * Checks if the Participant has a PBA account.
     * 
     * @return true if the participant has a PBA account
     */
    public Boolean getParticipantHasPbaMoney() {
        return participantHasPbaMoney;
    }

    /**
     * Sets the PBA account indicator.
     * 
     * @param participantHasPbaMoney true if the participant has a PBA account
     */
    public void setParticipantHasPbaMoney(final Boolean hasPBAAccount) {
        this.participantHasPbaMoney = hasPBAAccount;
    }

    /**
     * Returns the value of the Cheque Payable To code as follows: CL = Client, PR = Participant, TR =
     * Trustee.
     * 
     * @return 2 character code indicating the payee
     */
    public String getChequePayableToCode() {
        return chequePayableToCode;
    }

    /**
     * Sets the Cheque Payable To 2-character code. Valid values: CL = Client, PR = Participant, TR =
     * Trustee.
     * 
     * @param chequePayableToCode The chequePayableToCode to set.
     */
    public void setChequePayableToCode(final String chequePayableToCode) {
        this.chequePayableToCode = chequePayableToCode;
    }

//    /**
//     * Returns the pension plan name.
//     * 
//     * @return Pension plan name
//     */
//    public String getPlanName() {
//        return planName;
//    }
//
//    /**
//     * Sets the pension plan name.
//     * 
//     * @param planName The plan name to set.
//     */
//    public void setPlanName(final String planName) {
//        this.planName = planName;
//    }

    /**
     * Returns the contract name.
     * 
     * @return contractName
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * Sets the contract name.
     * 
     * @param contractName The contract name to set.
     */
    public void setContractName(final String contractName) {
        this.contractName = contractName;
    }

    /**
     * Returns the contract status code.
     * 
     * @return contractStatusCode
     */
    public String getContractStatusCode() {
        return contractStatusCode;
    }

    /**
     * Sets the contract status code.
     * 
     * @param contractStatusCode The contract status code to set.
     */
    public void setContractStatusCode(final String contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    /**
     * Returns the participant status code.
     * 
     * @return participantStatusCode
     */
    public String getParticipantStatusCode() {
        return participantStatusCode;
    }

    /**
     * Queries if the participant status is total.
     * 
     * @return boolean - True if the status is true, false otherwise.
     */
    public boolean isParticipantStatusTotal() {

        return StringUtils.equals(participantStatusCode,
                ParticipantInfo.PARTICIPANT_STATUS_DEATH_TOTAL)
                || StringUtils.equals(participantStatusCode,
                        ParticipantInfo.PARTICIPANT_STATUS_DISABILITY_TOTAL)
                || StringUtils.equals(participantStatusCode,
                        ParticipantInfo.PARTICIPANT_STATUS_RETIRED_TOTAL)
                || StringUtils.equals(participantStatusCode,
                        ParticipantInfo.PARTICIPANT_STATUS_TERMINATED_TOTAL)
                || StringUtils.equals(participantStatusCode,
                        ParticipantInfo.PARTICIPANT_STATUS_TERMINATION_OF_PARTICIPANT_PAID_UP)
                || StringUtils.equals(participantStatusCode,
                        		ParticipantInfo.PARTICIPANT_STATUS_OPTED_OUT);
                
    }

    /**
     * Sets the participant status code.
     * 
     * @param participantStatusCode The participant status code to set.
     */
    public void setParticipantStatusCode(final String participantStatusCode) {
        this.participantStatusCode = participantStatusCode;
    }

    /**
     * Returns the available money type balances.
     * 
     * @return Collection of WithdrawalRequestMoneyType
     */
    public Collection<WithdrawalRequestMoneyType> getMoneyTypes() {
        return moneyTypes;
    }

    /**
     * Sets the available money type balances excluding loans and PBAs.
     * 
     * @param moneyTypes - Collection of WithdrawalRequestMoneyType
     */
    public void setMoneyTypes(final Collection<WithdrawalRequestMoneyType> moneyTypes) {
        this.moneyTypes = (moneyTypes == null) ? new ArrayList<WithdrawalRequestMoneyType>()
                : moneyTypes;
    }

    /**
     * @return the systemOfRecordDateOfBirth
     */
    public Date getSystemOfRecordDateOfBirth() {
        return systemOfRecordDateOfBirth;
    }

    /**
     * @param systemOfRecordDateOfBirth the systemOfRecordDateOfBirth to set
     */
    public void setSystemOfRecordDateOfBirth(final Date systemOfRecordDateOfBirth) {
        this.systemOfRecordDateOfBirth = systemOfRecordDateOfBirth;
    }

    /**
     * @return the manulifeCompanyId
     */
    public String getManulifeCompanyId() {
        return manulifeCompanyId;
    }

    /**
     * @param manulifeCompanyId the manulifeCompanyId to set
     */
    public void setManulifeCompanyId(final String manulifeCompanyId) {
        this.manulifeCompanyId = manulifeCompanyId;
    }

    /**
     * @return Map of generic Money Type aliasess keyed by MoneyType code
     */
    public Map<String, String> getMoneyTypeAliases() {
        return moneyTypeAliases;
    }

    /**
     * @param Map of generic Money Type aliasess to set
     */
    public void setMoneyTypeAliases(Map<String, String> moneyTypeAliases) {
        this.moneyTypeAliases = moneyTypeAliases;
    }

    /**
     * @return the participantHasRothMoney
     */
    public Boolean getParticipantHasRothMoney() {
        return participantHasRothMoney;
    }

    /**
     * @param participantHasRothMoney the participantHasRothMoney to set
     */
    public void setParticipantHasRothMoney(Boolean hasRothMoney) {
        this.participantHasRothMoney = hasRothMoney;
    }
    /**
     * @return the hasExistingWithdrawalRequest
     */
    public Boolean getHasExistingWithdrawalRequest() {
        return hasExistingWithdrawalRequest;
    }

    /**
     * @param hasExistingWithdrawalRequest the hasExistingWithdrawalRequest to set
     */
    public void setHasExistingWithdrawalRequest(Boolean hasExistingWithdrawalRequest) {
        this.hasExistingWithdrawalRequest = hasExistingWithdrawalRequest;
    }

//    /**
//     * Queries if the participant has total status.
//     */
//    public boolean hasTotalStatus() {
//
//        final String statusCode = getParticipantStatusCode();
//        return (StringUtils.equals(statusCode, PARTICIPANT_STATUS_DEATH_TOTAL)
//                || StringUtils.equals(statusCode, PARTICIPANT_STATUS_DISABILITY_TOTAL)
//                || StringUtils.equals(statusCode, PARTICIPANT_STATUS_RETIRED_TOTAL)
//                || StringUtils.equals(statusCode, PARTICIPANT_STATUS_TERMINATED_TOTAL) || StringUtils
//                .equals(statusCode, PARTICIPANT_STATUS_TERMINATION_OF_PARTICIPANT_PAID_UP)
//                || StringUtils.equals(statusCode,
//                		ParticipantInfo.PARTICIPANT_STATUS_OPTED_OUT));
//    }

    /**
     * @return the participantEnrollmentDate
     */
    public Date getParticipantEnrollmentDate() {
        return participantEnrollmentDate;
    }

    /**
     * @param participantEnrollmentDate the participantEnrollmentDate to set
     */
    public void setParticipantEnrollmentDate(Date participantEnrollmentDate) {
        this.participantEnrollmentDate = participantEnrollmentDate;
    }
}
