package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;

/**
 * VO for Notice Manager Plan Sponsor web site report visitor statistics.
 * 
 */
public class PlanSponsorWebsiteReportVisitorsStatsVO implements Serializable {

    private static final long serialVersionUID = -3385028674744152479L;
    private String statisticDescription;
    private int planSponsorCount;
    private int tpaCount;
    private int intermediaryContactCount;
    private int totalCareCount;

    /**
     * Constructor.
     * 
     * @param statisticDescription
     */
    public PlanSponsorWebsiteReportVisitorsStatsVO(String statisticDescription) {
        super();
        this.statisticDescription = statisticDescription;
    }

    /**
     * Default Constructor.
     * 
     */
    public PlanSponsorWebsiteReportVisitorsStatsVO() {
        super();
    }

    /**
     * Gets Statistic Description.
     * 
     * @return statisticDescription
     */
    public String getStatisticDescription() {
        return statisticDescription;
    }

    /**
     * Sets Statistic Description.
     * 
     * @param statisticDescription
     */
    public void setStatisticDescription(String statisticDescription) {
        this.statisticDescription = statisticDescription;
    }

    /**
     * Gets Plan Sponsor Count.
     * 
     * @return planSponsorCount
     */
    public int getPlanSponsorCount() {
        return planSponsorCount;
    }

    /**
     * Sets Plan Sponsor Count.
     * 
     * @param planSponsorCount
     */
    public void setPlanSponsorCount(int planSponsorCount) {
        this.planSponsorCount = planSponsorCount;
    }

    /**
     * Gets TPA Count.
     * 
     * @return tpaCount.
     */
    public int getTpaCount() {
        return tpaCount;
    }

    /**
     * Sets TPA Count.
     * 
     * @param tpaCount
     */
    public void setTpaCount(int tpaCount) {
        this.tpaCount = tpaCount;
    }

    /**
     * Gets Intermediary Contact Count.
     * 
     * @return intermediaryContactCount
     */
    public int getIntermediaryContactCount() {
        return intermediaryContactCount;
    }

    /**
     * Sets Intermediary Contact Count.
     * 
     * @param intermediaryContactCount
     */
    public void setIntermediaryContactCount(int intermediaryContactCount) {
        this.intermediaryContactCount = intermediaryContactCount;
    }

    /**
     * Gets Total Care Count.
     * 
     * @return totalCareCount.
     */
    public int getTotalCareCount() {
        return totalCareCount;
    }

    /**
     * Sets Total Care Count.
     * 
     * @param totalCareCount
     */
    public void setTotalCareCount(int totalCareCount) {
        this.totalCareCount = totalCareCount;
    }

}
