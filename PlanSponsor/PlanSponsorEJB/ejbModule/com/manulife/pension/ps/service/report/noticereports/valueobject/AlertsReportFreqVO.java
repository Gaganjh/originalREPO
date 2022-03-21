package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VO for alerts report frequency data.
 * 
 */
public class AlertsReportFreqVO implements Serializable {

    private static final long serialVersionUID = 411825576264897613L;

    private String frequency;

    private Integer numberOfAlertsPerFrequency;

    private BigDecimal alertFrequencyPercentage;

    /**
     * Constructor.
     * 
     * @param frequency
     */
    public AlertsReportFreqVO(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Constructor.
     * 
     * @param frequency
     * @param numberOfAlertsPerFrequency
     */
    public AlertsReportFreqVO(String frequency, int numberOfAlertsPerFrequency) {
        super();
        this.frequency = frequency;
        this.numberOfAlertsPerFrequency = numberOfAlertsPerFrequency;
    }

    /**
     * Returns the Alert frequency.
     * 
     * @return frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the Alert frequency.
     * 
     * @param frequency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns Number Of Alerts Per Frequency.
     * 
     * @return numberOfAlertsPerFrequency
     */
    public Integer getNumberOfAlertsPerFrequency() {
        return numberOfAlertsPerFrequency;
    }

    /**
     * Sets Number Of Alerts Per Frequency.
     * 
     * @param numberOfAlertsPerFrequency
     */
    public void setNumberOfAlertsPerFrequency(Integer numberOfAlertsPerFrequency) {
        this.numberOfAlertsPerFrequency = numberOfAlertsPerFrequency;
    }

    /**
     * Returns Alert Frequency Percentage.
     * 
     * @return alertFrequencyPercentage
     */
    public BigDecimal getAlertFrequencyPercentage() {
        return alertFrequencyPercentage;
    }

    /**
     * Sets Alert Frequency Percentage
     * 
     * @param alertFrequencyPercentage
     */
    public void setAlertFrequencyPercentage(BigDecimal alertFrequencyPercentage) {
        this.alertFrequencyPercentage = alertFrequencyPercentage;
    }

}
