package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;

/**
 * VO for Plan Sponsor web site pages visited by user category.
 * 
 */
public class PlanSponsorWebsiteReportPagesVisitedVO implements Serializable {

    private static final long serialVersionUID = 6095137342161800321L;
    private String pageName;
    private int planSponsorCount;
    private int tpaCount;
    private int intermediaryContactCount;
    private int totalCareCount;

    /**
     * Constructor.
     * 
     * @param pageName
     */
    public PlanSponsorWebsiteReportPagesVisitedVO(String pageName) {
        super();
        this.pageName = pageName;
    }

    /**
     * Default Constructor.
     * 
     */
    public PlanSponsorWebsiteReportPagesVisitedVO() {
        super();

    }

    /**
     * Gets page name.
     * 
     * @return pageName
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * Sets Page Name.
     * 
     * @param pageName
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /**
     * Gets Plan Sponsor Count
     * 
     * @return planSponsorCount
     */
    public int getPlanSponsorCount() {
        return planSponsorCount;
    }

    /**
     * Sets Plan Sponsor Count
     * 
     * @param planSponsorCount
     */
    public void setPlanSponsorCount(int planSponsorCount) {
        this.planSponsorCount = planSponsorCount;
    }

    /**
     * Gets TPA Count.
     * 
     * @return tpaCount
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
     * @return intermediaryContactCount.
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
     * @return totalCareCount
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
