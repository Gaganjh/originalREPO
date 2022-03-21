package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;
import java.util.List;

/**
 * VO that hold data for months with visits by user category
 * 
 */
public class PlanSponsorWebsiteReportMonthVisitsVO implements Serializable {

    private static final long serialVersionUID = -2901464091139351722L;
    private String description;
    private List<String> planSponsorMonths;
    private List<String> intermediaryContactMonths;
    private List<String> tpaMonths;
    private List<String> totalCareMonths;

    /**
     * Default Constructor.
     */
    public PlanSponsorWebsiteReportMonthVisitsVO() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param description
     */
    public PlanSponsorWebsiteReportMonthVisitsVO(String description) {
        super();
        this.description = description;
    }

    /**
     * Gets description.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets Plan sponsor most visited month(s).
     * 
     * @return planSponsorMonths.
     */
    public List<String> getPlanSponsorMonths() {
        return planSponsorMonths;
    }

    /**
     * Sets Plan sponsor most visited month(s).
     * 
     * @param planSponsorMonths
     * 
     */
    public void setPlanSponsorMonths(List<String> planSponsorMonths) {
        this.planSponsorMonths = planSponsorMonths;
    }

    /**
     * Gets Intermediary Contact most visited month(s).
     * 
     * @return IntermediaryContactMonths.
     */
    public List<String> getIntermediaryContactMonths() {
        return intermediaryContactMonths;
    }

    /**
     * Sets Intermediary Contact most visited month(s).
     * 
     * @param intermediaryContactMonths
     */
    public void setIntermediaryContactMonths(List<String> intermediaryContactMonths) {
        this.intermediaryContactMonths = intermediaryContactMonths;
    }

    /**
     * Gets TPA most visited month(s).
     * 
     * @return tpaMonths.
     */
    public List<String> getTpaMonths() {
        return tpaMonths;
    }

    /**
     * Sets TPA most visited month(s).
     * 
     * @param tpaMonths
     */
    public void setTpaMonths(List<String> tpaMonths) {
        this.tpaMonths = tpaMonths;
    }

    /**
     * Gets TPA most visited month(s).
     * 
     * @return totalCareMonths.
     */
    public List<String> getTotalCareMonths() {
        return totalCareMonths;
    }

    /**
     * Sets Total Care most visited month(s).
     * 
     * @param totalCareMonths
     */
    public void setTotalCareMonths(List<String> totalCareMonths) {
        this.totalCareMonths = totalCareMonths;
    }

}
