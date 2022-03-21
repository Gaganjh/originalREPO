package com.manulife.pension.bd.service.brokerListing.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This class will hold the Broker Listing Main Report Data.
 * 
 * @author harlomte
 * 
 */
public class BrokerListingReportVO implements Serializable {
    
    private static final long serialVersionUID = -2489151442179220137L;

    String financialRepName;

    String firmName;

    String city;

    String state;

    String zipCode;

    String producerCode;

    BigDecimal numOfContracts;

    BigDecimal totalAssets;
    
    String partyID;

    public String getFinancialRepName() {
        return financialRepName;
    }

    public void setFinancialRepName(String financialRepName) {
        this.financialRepName = financialRepName;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProducerCode() {
        return producerCode;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public BigDecimal getNumOfContracts() {
        return numOfContracts;
    }

    public void setNumOfContracts(BigDecimal numOfContracts) {
        this.numOfContracts = numOfContracts;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public String getPartyID() {
        return partyID;
    }

    public void setPartyID(String partyID) {
        this.partyID = partyID;
    }
    
}
