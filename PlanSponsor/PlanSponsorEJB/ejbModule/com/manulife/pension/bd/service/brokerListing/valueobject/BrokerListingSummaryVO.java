package com.manulife.pension.bd.service.brokerListing.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This class will hold the Broker Listing Summary Information.
 * 
 * @author harlomte
 * 
 */
public class BrokerListingSummaryVO implements Serializable {
    
    private static final long serialVersionUID = -1923477097870777890L;

    String internalUserName; 

    String associatedBrokerDealerFirmName; 
    
    BigDecimal totalContractAssets;

    BigDecimal totalNumberOfContracts;

    BigDecimal totalNumberOfFinancialReps;

    public String getInternalUserName() {
        return internalUserName;
    }

    public void setInternalUserName(String internalUserName) {
        this.internalUserName = internalUserName;
    }

    public String getAssociatedBrokerDealerFirmName() {
        return associatedBrokerDealerFirmName;
    }

    public void setAssociatedBrokerDealerFirmName(String associatedBrokerDealerFirmName) {
        this.associatedBrokerDealerFirmName = associatedBrokerDealerFirmName;
    }

    public BigDecimal getTotalContractAssets() {
        return totalContractAssets;
    }

    public void setTotalContractAssets(BigDecimal totalContractAssets) {
        this.totalContractAssets = totalContractAssets;
    }

    public BigDecimal getTotalNumberOfContracts() {
        return totalNumberOfContracts;
    }

    public void setTotalNumberOfContracts(BigDecimal totalNumberOfContracts) {
        this.totalNumberOfContracts = totalNumberOfContracts;
    }

    public BigDecimal getTotalNumberOfFinancialReps() {
        return totalNumberOfFinancialReps;
    }

    public void setTotalNumberOfFinancialReps(BigDecimal totalNumberOfFinancialReps) {
        this.totalNumberOfFinancialReps = totalNumberOfFinancialReps;
    }


}
