package com.manulife.pension.ps.service.report.tpabob.valueobject;

import java.math.BigDecimal;

/**
 * This class will hold the contract related information to be displayed in TPA BOB Summary section.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessSummaryContractVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // Contract related information to be displayed in TPA BOB Summary section.
    String firmName;

    String firmNumber;

    BigDecimal activeContractCount = new BigDecimal("0.0");

    BigDecimal activeContractLives = new BigDecimal("0.0");

    BigDecimal activeContractAssets = new BigDecimal("0.0");

    BigDecimal pendingContractCount = new BigDecimal("0.0");

    BigDecimal pendingContractLives = new BigDecimal("0.0");

    BigDecimal discontinuedContractCount = new BigDecimal("0.0");

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmNumber() {
        return firmNumber;
    }

    public void setFirmNumber(String firmNumber) {
        this.firmNumber = firmNumber;
    }

    public BigDecimal getActiveContractCount() {
        return activeContractCount == null ? new BigDecimal("0.0") : activeContractCount;
    }

    public void setActiveContractCount(BigDecimal activeContractCount) {
        this.activeContractCount = activeContractCount;
    }

    public BigDecimal getActiveContractLives() {
        return activeContractLives == null ? new BigDecimal("0.0") : activeContractLives;
    }

    public void setActiveContractLives(BigDecimal activeContractLives) {
        this.activeContractLives = activeContractLives;
    }

    public BigDecimal getActiveContractAssets() {
        return activeContractAssets == null ? new BigDecimal("0.0") : activeContractAssets;
    }

    public void setActiveContractAssets(BigDecimal activeContractAssets) {
        this.activeContractAssets = activeContractAssets;
    }

    public BigDecimal getPendingContractCount() {
        return pendingContractCount == null ? new BigDecimal("0.0") : pendingContractCount;
    }

    public void setPendingContractCount(BigDecimal pendingContractCount) {
        this.pendingContractCount = pendingContractCount;
    }

    public BigDecimal getPendingContractLives() {
        return pendingContractLives == null ? new BigDecimal("0.0") : pendingContractLives;
    }

    public void setPendingContractLives(BigDecimal pendingContractLives) {
        this.pendingContractLives = pendingContractLives;
    }

    public BigDecimal getDiscontinuedContractCount() {
        return discontinuedContractCount == null ? new BigDecimal("0.0") : discontinuedContractCount;
    }

    public void setDiscontinuedContractCount(BigDecimal discontinuedContractCount) {
        this.discontinuedContractCount = discontinuedContractCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
