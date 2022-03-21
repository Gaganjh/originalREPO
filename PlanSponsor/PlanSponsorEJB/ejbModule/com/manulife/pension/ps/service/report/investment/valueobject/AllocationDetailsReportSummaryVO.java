package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

public class AllocationDetailsReportSummaryVO implements Serializable {

    private String fundId;
    private String fundName;
    private String fundType;
    private String rateType;
    private BigDecimal participantsCount;
    private BigDecimal employeeAssetsTotal;
    private BigDecimal employerAssetsTotal;
    private BigDecimal assetsTotal;
    private String fundClass;

    public AllocationDetailsReportSummaryVO(String fundId, String fundName,
    	String fundType, String rateType,
        BigDecimal participantsCount, BigDecimal employeeAssetsTotal,
        BigDecimal employerAssetsTotal, BigDecimal assetsTotal) {

        this.fundId = fundId;
        this.fundName = fundName;
        this.fundType = fundType;
        this.participantsCount = participantsCount;
        this.employeeAssetsTotal = employeeAssetsTotal;
        this.employerAssetsTotal = employerAssetsTotal;
        this.assetsTotal = assetsTotal;
        this.rateType =rateType;
    }
    

    /**
     * Gets the fundId
     * @return Returns an BigDecimal
     */
    public String getFundId() {
        return fundId;
    }


    /**
     * Gets the fundName
     * @return Returns a String
     */
    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    /**
     * Gets the fundType
     * @return Returns a String
     */
    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    /**
     * Gets the participants count
     * @return Returns a BigDecimal
     */
    public BigDecimal getParticipantsCount() {
        return participantsCount;
    }

    /**
     * Gets the employee assets total
     * @return Returns a BigDecimal
     */
    public BigDecimal getEmployeeAssetsTotal() {
        return employeeAssetsTotal;
    }

    /**
     * Gets the employer assets total
     * @return Returns a BigDecimal
     */
    public BigDecimal getEmployerAssetsTotal() {
        return employerAssetsTotal;
    }

    /**
     * Gets the assets total
     * @return Returns a BigDecimal
     */
    public BigDecimal getAssetsTotal() {
        return assetsTotal;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("fundId=").append(fundId).append(";");
        sb.append("fundName=").append(fundName).append(";");
        sb.append("fundType=").append(fundType).append(";");
        sb.append("rateType=").append(rateType).append(";");
        sb.append("participantsCount=").append(participantsCount).append(";");
        sb.append("employeeAssetsTotal=").append(employeeAssetsTotal).append(";");
        sb.append("employerAssetsTotal=").append(employerAssetsTotal).append(";");
        sb.append("assetsTotal=").append(assetsTotal).append(";");

        return sb.toString();
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRateType() {
        return rateType;
    }

    /**
     * Sets the asset class
     * 
     * @param assetClass String
     */
    public void setFundClass(String fundClass) {
        this.fundClass = fundClass;
    }

    /**
     * Gets the asset class
     * 
     * @return Returns a String
     */
    public String getFundClass() {
        return fundClass;
    } 
	
}