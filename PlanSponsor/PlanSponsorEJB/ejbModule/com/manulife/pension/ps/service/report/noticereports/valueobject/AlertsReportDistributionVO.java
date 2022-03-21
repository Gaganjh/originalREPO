package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;

/**
 * VO for alerts report's alert distribution by month.
 * 
 */
public class AlertsReportDistributionVO implements Serializable {

    private static final long serialVersionUID = -1277711351582057180L;

    private String month;
    private int year;

    private int numberOfAlerts;

    private boolean monthInSearchRange = true;

    /**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
     * Constructor
     * 
     * @param month
     */
    public AlertsReportDistributionVO(String month,int year) {
        super();
        
        this.month = month;
        this.year = year;
    }

    /**
     * Gets Month of distribution.
     * 
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets distribution month.
     * 
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Gets Number Of Alerts
     * 
     * @return numberOfAlerts
     */
    public int getNumberOfAlerts() {
        return numberOfAlerts;
    }

    /**
     * Sets Number Of Alerts
     * 
     * @param numberOfAlerts
     */
    public void setNumberOfAlerts(int numberOfAlerts) {
        this.numberOfAlerts = numberOfAlerts;
    }

    /**
     * Checks whether the month is in search range.
     * 
     * @return true if the month is in search range.
     */
    public boolean isMonthInSearchRange() {
        return monthInSearchRange;
    }

    /**
     * Sets whether the month is in search range.
     * 
     * @param isMonthInSearchRange
     */
    public void setMonthInSearchRange(boolean monthInSearchRange) {
        this.monthInSearchRange = monthInSearchRange;
    }

}
