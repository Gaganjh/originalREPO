package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

public class AllocationDetailsReportDetailVO implements Serializable {

    // These are used to avoid hard coding constant string throughout the
    // application and to allow traceability.
    public interface keys {
        public static final String name = "name";
        public static final String ssn = "ssn";
        public static final String ongoingContributions = "ongoingContributions";
        public static final String employeeAssetsAmount = "employeeAssetsAmount";
        public static final String employerAssetsAmount = "employerAssetsAmount";
        public static final String totalAssetsAmount = "totalAssetsAmount";
    }

    private String participantId;
    private String name;
    private String ssn;
    private String ongoingContributions;
    private BigDecimal employeeAssetsAmount;
    private BigDecimal employerAssetsAmount;
    private BigDecimal totalAssetsAmount;

    public AllocationDetailsReportDetailVO(String participantId, String name, String ssn,
        String ongoingContributions, BigDecimal employeeAssetsAmount,
        BigDecimal employerAssetsAmount, BigDecimal totalAssetsAmount) {

        this.participantId = participantId;
        this.name = name;
        this.ssn = ssn;
        this.ongoingContributions = ongoingContributions;
        this.employeeAssetsAmount = employeeAssetsAmount;
        this.employerAssetsAmount = employerAssetsAmount;
        this.totalAssetsAmount = totalAssetsAmount;
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getOngoingContributions() {
        return ongoingContributions;
    }

    public BigDecimal getEmployeeAssetsAmount() {
        return employeeAssetsAmount;
    }

    public BigDecimal getEmployerAssetsAmount() {
        return employerAssetsAmount;
    }

    public BigDecimal getTotalAssetsAmount() {
        return totalAssetsAmount;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=").append(name).append(";");
        sb.append("ssn=").append(ssn).append(";");
        sb.append("ongoingContributions=").append(ongoingContributions).append(";");
        sb.append("employeeAssetsAmount=").append(employeeAssetsAmount).append(";");
        sb.append("employerAssetsAmount=").append(employerAssetsAmount).append(";");
        sb.append("totalAssetsAmount=").append(totalAssetsAmount).append(";");

        return sb.toString();
    }
}

