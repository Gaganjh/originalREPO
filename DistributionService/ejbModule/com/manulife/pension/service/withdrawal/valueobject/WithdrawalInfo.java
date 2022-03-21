/**
 * Created Sep 27 2006
 */
package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * Value object to hold the results returned by the WithdrawalInfo stored procedure. To be used in
 * the Beforeproceeding page.
 * 
 * @author mihai popa
 */
public class WithdrawalInfo extends BaseSerializableCloneableObject {

    /**
     * The default serialVersion UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     */
    public WithdrawalInfo() {
        super();
    }

    private String contractRothMoneyTypeId;

    private boolean participantHasRothMoney;

    private boolean personalBrokerageAccountInd;

    private BigDecimal totalBalance;

    private Date requestDate;

    private String contractStatus;

    private String participantStatus;

    private String initiatedBy;

    private int loanRequestId;

    private Date requestExpiryDate;

    private String loanRequestStatus;

    private Date apvDate;

    private Date apvExpiryDate;

    private boolean pbaMoneyTypeInd;

    private boolean loansAllowedInd;

    /**
     * @return the loansAllowedInd
     */
    public boolean isLoansAllowedInd() {
        return loansAllowedInd;
    }

    /**
     * @param loansAllowedInd the loansAllowedInd to set
     */
    public void setLoansAllowedInd(final boolean loansAllowedInd) {
        this.loansAllowedInd = loansAllowedInd;
    }

    /**
     * @return the pbaMoneyTypeInd
     */
    public boolean isPbaMoneyTypeInd() {
        return pbaMoneyTypeInd;
    }

    /**
     * @param pbaMoneyTypeInd the pbaMoneyTypeInd to set
     */
    public void setPbaMoneyTypeInd(final boolean pbaMoneyTypeInd) {
        this.pbaMoneyTypeInd = pbaMoneyTypeInd;
    }

    /**
     * @return Date - The apvDate.
     */
    public Date getApvDate() {
        return apvDate;
    }

    /**
     * @param apvDate - The apvDate to set.
     */
    public void setApvDate(final Date apvDate) {
        this.apvDate = apvDate;
    }

    /**
     * @return Date - The apvExpiryDate.
     */
    public Date getApvExpiryDate() {
        return apvExpiryDate;
    }

    /**
     * @param apvExpiryDate - The apvExpiryDate to set.
     */
    public void setApvExpiryDate(final Date apvExpiryDate) {
        this.apvExpiryDate = apvExpiryDate;
    }

    /**
     * @return String - The contractRothMoneyTypeId.
     */
    public String getContractRothMoneyTypeId() {
        return contractRothMoneyTypeId;
    }

    /**
     * @param contractRothMoneyTypeId - The contractRothMoneyTypeId to set.
     */
    public void setContractRothMoneyTypeId(final String contractRothMoneyTypeId) {
        this.contractRothMoneyTypeId = contractRothMoneyTypeId;
    }

    /**
     * @return String - The contractStatus.
     */
    public String getContractStatus() {
        return contractStatus;
    }

    /**
     * @param contractStatus - The contractStatus to set.
     */
    public void setContractStatus(final String contractStatus) {
        this.contractStatus = contractStatus;
    }

    /**
     * @return String - The initiatedBy.
     */
    public String getInitiatedBy() {
        return initiatedBy;
    }

    /**
     * @param initiatedBy - The initiatedBy to set.
     */
    public void setInitiatedBy(final String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    /**
     * @return int - The loanRequestId.
     */
    public int getLoanRequestId() {
        return loanRequestId;
    }

    /**
     * @param loanRequestId - The loanRequestId to set.
     */
    public void setLoanRequestId(final int loanRequestId) {
        this.loanRequestId = loanRequestId;
    }

    /**
     * @return String - The loanRequestStatus.
     */
    public String getLoanRequestStatus() {
        return loanRequestStatus;
    }

    /**
     * @param loanRequestStatus - The loanRequestStatus to set.
     */
    public void setLoanRequestStatus(final String loanRequestStatus) {
        this.loanRequestStatus = loanRequestStatus;
    }

    /**
     * @return boolean - The participantHasRothMoney.
     */
    public boolean isParticipantHasRothMoney() {
        return participantHasRothMoney;
    }

    /**
     * @param participantHasRothMoney - The participantHasRothMoney to set.
     */
    public void setParticipantHasRothMoney(final boolean participantHasRothMoney) {
        this.participantHasRothMoney = participantHasRothMoney;
    }

    /**
     * @return String - The participantStatus.
     */
    public String getParticipantStatus() {
        return participantStatus;
    }

    /**
     * @param participantStatus - The participantStatus to set.
     */
    public void setParticipantStatus(final String participantStatus) {
        this.participantStatus = participantStatus;
    }

    /**
     * This is the PBA value for the contract. If it allows PBA, then this is true.
     * 
     * @return boolean - The personalBrokerageAccountInd.
     */
    public boolean getPersonalBrokerageAccountInd() {
        return personalBrokerageAccountInd;
    }

    /**
     * @param personalBrokerageAccountInd - The personalBrokerageAccountInd to set.
     */
    public void setPersonalBrokerageAccountInd(final boolean personalBrokerageAccountInd) {
        this.personalBrokerageAccountInd = personalBrokerageAccountInd;
    }

    /**
     * @return Date - The requestDate.
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate - The requestDate to set.
     */
    public void setRequestDate(final Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return Date - The requestExpiryDate.
     */
    public Date getRequestExpiryDate() {
        return requestExpiryDate;
    }

    /**
     * @param requestExpiryDate - The requestExpiryDate to set.
     */
    public void setRequestExpiryDate(final Date requestExpiryDate) {
        this.requestExpiryDate = requestExpiryDate;
    }

    /**
     * @return BigDecimal - The totalBalance.
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance - The totalBalance to set.
     */
    public void setTotalBalance(final BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

}
