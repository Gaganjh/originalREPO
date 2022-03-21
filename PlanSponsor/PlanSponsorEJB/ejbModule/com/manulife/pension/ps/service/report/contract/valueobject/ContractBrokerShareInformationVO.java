package com.manulife.pension.ps.service.report.contract.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class ContractBrokerShareInformationVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String brokerFirstName;
    private String brokerLastName;
    private BigDecimal assetBasedComission;
    private BigDecimal tranferCommissionAccrued;
    private BigDecimal tranferCommissionUpfront;
    private BigDecimal regularCommissionAccrued;
    private BigDecimal regularCommissionUpfront;
    private BigDecimal priceCredit;

    public String getBrokerFirstName() {
        return brokerFirstName;
    }

    public void setBrokerFirstName(String brokerFirstName) {
        this.brokerFirstName = brokerFirstName;
    }

    public String getBrokerLastName() {
        return brokerLastName;
    }

    public void setBrokerLastName(String brokerLastName) {
        this.brokerLastName = brokerLastName;
    }

    public BigDecimal getTranferCommission() {
        if (tranferCommissionAccrued != null) {
            return tranferCommissionAccrued;
        } else if (tranferCommissionUpfront != null) {
            return tranferCommissionUpfront;
        } else {
            return new BigDecimal(0.0);
        }
    }


    public BigDecimal getRegularCommission() {
        if (regularCommissionAccrued != null) {
            return regularCommissionAccrued;
        } else if (regularCommissionUpfront != null) {
            return regularCommissionUpfront;
        } else {
            return new BigDecimal(0.0);
        }
    }


    public BigDecimal getAssetBasedComission() {
        if (assetBasedComission != null) {
            return assetBasedComission;
        } else {
            return new BigDecimal(0.0);
        }
    }

    public void setAssetBasedComission(BigDecimal assetBasedComission) {
        this.assetBasedComission = assetBasedComission;
    }

    public BigDecimal getPriceCredit() {
        if (priceCredit != null) {
            return priceCredit;
        } else {
            return new BigDecimal(0.0);
        }
    }

    public void setPriceCredit(BigDecimal priceCredit) {
        this.priceCredit = priceCredit;
    }

    public String getBrokerName() {
        return StringUtils.trim(brokerFirstName) + " " + StringUtils.trim(brokerLastName);
    }

    public BigDecimal getTranferCommissionAccrued() {
        return tranferCommissionAccrued;
    }

    public void setTranferCommissionAccrued(BigDecimal tranferCommissionAccrued) {
        this.tranferCommissionAccrued = tranferCommissionAccrued;
    }

    public BigDecimal getTranferCommissionUpfront() {
        return tranferCommissionUpfront;
    }

    public void setTranferCommissionUpfront(BigDecimal tranferCommissionUpfront) {
        this.tranferCommissionUpfront = tranferCommissionUpfront;
    }

    public BigDecimal getRegularCommissionAccrued() {
        return regularCommissionAccrued;
    }

    public void setRegularCommissionAccrued(BigDecimal regularCommissionAccrued) {
        this.regularCommissionAccrued = regularCommissionAccrued;
    }

    public BigDecimal getRegularCommissionUpfront() {
        return regularCommissionUpfront;
    }

    public void setRegularCommissionUpfront(BigDecimal regularCommissionUpfront) {
        this.regularCommissionUpfront = regularCommissionUpfront;
    }
}
