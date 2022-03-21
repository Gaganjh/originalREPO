package com.manulife.pension.ps.service.report.tpabob.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * This class will hold the information to be displayed in TPA BOB Summary section.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessSummaryVO implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;

    // This will hold the Contract Information to be displayed in Summary section.
    ArrayList<TPABlockOfBusinessSummaryContractVO> tpaBobSummaryContractVO;
    
    // These will hold the total values to be shown in summary section.
    BigDecimal totalActiveContractCount;

    BigDecimal totalActiveContractLives;

    BigDecimal totalActiveContractAssets;

    BigDecimal totalPendingContractCount;

    BigDecimal totalPendingContractLives;

    public ArrayList<TPABlockOfBusinessSummaryContractVO> getTpaBobSummaryContractVO() {
        return tpaBobSummaryContractVO;
    }

    public void setTpaBobSummaryContractVO(
            ArrayList<TPABlockOfBusinessSummaryContractVO> tpaBobSummaryContractVO) {
        this.tpaBobSummaryContractVO = tpaBobSummaryContractVO;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigDecimal getTotalActiveContractCount() {
        return totalActiveContractCount == null ? new BigDecimal("0.0") : totalActiveContractCount;
    }

    public void setTotalActiveContractCount(BigDecimal totalActiveContractCount) {
        this.totalActiveContractCount = totalActiveContractCount;
    }

    public BigDecimal getTotalActiveContractLives() {
        return totalActiveContractLives == null ? new BigDecimal("0.0") : totalActiveContractLives;
    }

    public void setTotalActiveContractLives(BigDecimal totalActiveContractLives) {
        this.totalActiveContractLives = totalActiveContractLives;
    }

    public BigDecimal getTotalActiveContractAssets() {
        return totalActiveContractAssets == null ? new BigDecimal("0.0") : totalActiveContractAssets;
    }

    public void setTotalActiveContractAssets(BigDecimal totalActiveContractAssets) {
        this.totalActiveContractAssets = totalActiveContractAssets;
    }

    public BigDecimal getTotalPendingContractCount() {
        return totalPendingContractCount == null ? new BigDecimal("0.0") : totalPendingContractCount;
    }

    public void setTotalPendingContractCount(BigDecimal totalPendingContractCount) {
        this.totalPendingContractCount = totalPendingContractCount;
    }

    public BigDecimal getTotalPendingContractLives() {
        return totalPendingContractLives == null ? new BigDecimal("0.0") : totalPendingContractLives;
    }

    public void setTotalPendingContractLives(BigDecimal totalPendingContractLives) {
        this.totalPendingContractLives = totalPendingContractLives;
    }
}
