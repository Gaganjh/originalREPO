package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.util.ServiceFeatureConstants;

public class TPAVestingSubmissionDetailsVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private BigDecimal  contractId;
    private String      contractName;
    private Date        planYearEnd;
    private BigDecimal  tpaFirmId;
    private String      tpaFirmName;
    private BigDecimal  submissionId;
    private Date        lastSubmissionDate;
    private String      lastSubmissionStatus;
    private String      vestingServiceFeature;
    private Date        earliestProvidedDate;
    private Date        latestProvidedDate;
    private Date        earliestCalculatedDate;
    private Date        latestCalculatedDate;
    private Date        calcOnlineUpdateDate;
    private String      calcOnlineUserId;
    private Date        provOnlineUpdateDate;
    private String      provOnlineUserId;
    
    
    
    public BigDecimal getContractId() {
        return contractId;
    }
    public void setContractId(BigDecimal contractId) {
        this.contractId = contractId;
    }
    public String getContractName() {
        return contractName;
    }
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    public Date getPlanYearEnd() {
        return planYearEnd;
    }
    public void setPlanYearEnd(Date planYearEnd) {
        this.planYearEnd = planYearEnd;
    }
    public BigDecimal getTpaFirmId() {
        return tpaFirmId;
    }
    public void setTpaFirmId(BigDecimal tpaFirmId) {
        this.tpaFirmId = tpaFirmId;
    }
    public String getTpaFirmName() {
        return tpaFirmName;
    }
    public void setTpaFirmName(String tpaFirmName) {
        this.tpaFirmName = tpaFirmName;
    }
    public Date getLastSubmissionDate() {
        return lastSubmissionDate;
    }
    public void setLastSubmissionDate(Date lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }
    public String getLastSubmissionStatus() {
        return lastSubmissionStatus;
    }
    public void setLastSubmissionStatus(String lastSubmissionStatus) {
        this.lastSubmissionStatus = lastSubmissionStatus;
    }
    public String getVestingServiceFeature() {
        return vestingServiceFeature;
    }
    public void setVestingServiceFeature(String vestingServiceFeature) {
        this.vestingServiceFeature = vestingServiceFeature;
    }
    public Date getEarliestCalculatedDate() {
        return earliestCalculatedDate;
    }
    public void setEarliestCalculatedDate(Date earliestCalculatedDate) {
        this.earliestCalculatedDate = earliestCalculatedDate;
    }
    public Date getEarliestProvidedDate() {
        return earliestProvidedDate;
    }
    public void setEarliestProvidedDate(Date earliestProvidedDate) {
        this.earliestProvidedDate = earliestProvidedDate;
    }
    public Date getLatestCalculatedDate() {
        return latestCalculatedDate;
    }
    public void setLatestCalculatedDate(Date latestCalculatedDate) {
        this.latestCalculatedDate = latestCalculatedDate;
    }
    public Date getLatestProvidedDate() {
        return latestProvidedDate;
    }
    public void setLatestProvidedDate(Date latestProvidedDate) {
        this.latestProvidedDate = latestProvidedDate;
    }
    public BigDecimal getSubmissionId() {
        return submissionId;
    }
    public void setSubmissionId(BigDecimal submissionId) {
        this.submissionId = submissionId;
    }
    
    public Date getEarliestDate() {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            return getEarliestProvidedDate();
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            return getEarliestCalculatedDate();
        }
        return null;
    }
    
    public Date getLatestDate() {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            return getLatestProvidedDate();
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            return getLatestCalculatedDate();
        }
        return null;
    }
    
    public Date getOnlineUpdateDate() {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            return getProvOnlineUpdateDate();
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            return getCalcOnlineUpdateDate();
        }
        return null;
    }
    
    public void setOnlineUpdateDate(Date inDate) {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            setProvOnlineUpdateDate(inDate);
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            setCalcOnlineUpdateDate(inDate);
        }
    }
    
    public String getOnlineUserId() {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            return getProvOnlineUserId();
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            return getCalcOnlineUserId();
        }
        return null;
    }
    
    public void setOnlineUserId(String userId) {
        if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.PROVIDED)) {
            setProvOnlineUserId(userId);
        } else if (StringUtils.equals(vestingServiceFeature, ServiceFeatureConstants.CALCULATED)) {
            setCalcOnlineUserId(userId);
        }
    }
    
    public String getComments() {
        String flag = "";
        if (getLastSubmissionDate() == null &&
            getOnlineUpdateDate() == null) {
            flag += "0";
        } else {
            if (!ObjectUtils.equals(getEarliestDate(), getLatestDate())) {
                flag += "1";
            }
            if (getOnlineUpdateDate() != null) {
                flag += "2";
            }
            
        }
        return flag;
    }
    
    public Date getCalcOnlineUpdateDate() {
        return calcOnlineUpdateDate;
    }
    public void setCalcOnlineUpdateDate(Date calcOnlineUpdateDate) {
        this.calcOnlineUpdateDate = calcOnlineUpdateDate;
    }
    public String getCalcOnlineUserId() {
        return calcOnlineUserId;
    }
    public void setCalcOnlineUserId(String calcOnlineUserId) {
        this.calcOnlineUserId = calcOnlineUserId;
    }
    public Date getProvOnlineUpdateDate() {
        return provOnlineUpdateDate;
    }
    public void setProvOnlineUpdateDate(Date provOnlineUpdateDate) {
        this.provOnlineUpdateDate = provOnlineUpdateDate;
    }
    public String getProvOnlineUserId() {
        return provOnlineUserId;
    }
    public void setProvOnlineUserId(String provOnlineUserId) {
        this.provOnlineUserId = provOnlineUserId;
    }

}
