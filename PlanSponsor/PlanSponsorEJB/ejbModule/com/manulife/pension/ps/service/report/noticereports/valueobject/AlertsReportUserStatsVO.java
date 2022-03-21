package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VO for Notice Manager Alerts Control Report user category statistics.
 * 
 */
public class AlertsReportUserStatsVO implements Serializable {

    private static final long serialVersionUID = -327840094494183177L;

    private String users;

    private int numberOfAlertUsers;

    private BigDecimal averageNumberOfAlertsPerUser;

    private int totalAlertSetUp;

    private int urgentAlerts;

    private int normalAlerts;

    private int numberOfDeletedAlerts;

    /**
     * Constructor.
     * 
     * @param users
     * @param numberOfAlertUsers
     * @param averageNumberOfAlertsPerUser
     * @param totalAlertSetUp
     * @param urgentAlerts
     * @param normalAlerts
     */
    public AlertsReportUserStatsVO(String users, int numberOfAlertUsers,
            BigDecimal averageNumberOfAlertsPerUser, int totalAlertSetUp, int urgentAlerts,
            int normalAlerts) {
        super();
        this.users = users;
        this.numberOfAlertUsers = numberOfAlertUsers;
        this.averageNumberOfAlertsPerUser = averageNumberOfAlertsPerUser;
        this.totalAlertSetUp = totalAlertSetUp;
        this.urgentAlerts = urgentAlerts;
        this.normalAlerts = normalAlerts;
    }

    /**
     * Default Constructor.
     * 
     */
    public AlertsReportUserStatsVO() {

    }

    /**
     * Constructor.
     * 
     * @param users
     */
    public AlertsReportUserStatsVO(String users) {
        super();
        this.users = users;
    }

    /**
     * Returns users category.
     * 
     * @return users
     */
    public String getUsers() {
        return users;
    }

    /**
     * Sets users category.
     * 
     * @param users
     */
    public void setUsers(String users) {
        this.users = users;
    }

    /**
     * Returns number of Users who set up alerts for a specified Category.
     * 
     * @return numberOfAlertUsers
     */
    public int getNumberOfAlertUsers() {
        return numberOfAlertUsers;
    }

    /**
     * Sets number of Users who set up alerts for a specified user category.
     * 
     * @param numberOfAlertUsers
     */
    public void setNumberOfAlertUsers(int numberOfAlertUsers) {
        this.numberOfAlertUsers = numberOfAlertUsers;
    }

    /**
     * Returns total alerts setup for a specified user category.
     * 
     * @return totalAlertSetUp
     */
    public int getTotalAlertSetUp() {
        return totalAlertSetUp;
    }

    /**
     * Sets total alerts setup for a specified user category.
     * 
     * @param totalAlertSetUp
     */
    public void setTotalAlertSetUp(int totalAlertSetUp) {
        this.totalAlertSetUp = totalAlertSetUp;
    }

    /**
     * Returns number of urgent alerts.
     * 
     * @return urgentAlerts
     */
    public int getUrgentAlerts() {
        return urgentAlerts;
    }

    /**
     * Sets number of urgent alerts.
     * 
     * @param urgentAlerts
     */
    public void setUrgentAlerts(int urgentAlerts) {
        this.urgentAlerts = urgentAlerts;
    }

    /**
     * Returns number of normal alerts.
     * 
     * @return normalAlerts
     */
    public int getNormalAlerts() {
        return normalAlerts;
    }

    /**
     * Sets number of normal alerts.
     * 
     * @param normalAlerts
     */
    public void setNormalAlerts(int normalAlerts) {
        this.normalAlerts = normalAlerts;
    }

    /**
     * Returns number of deleted alerts.
     * 
     * @return numberOfDeletedAlerts
     */
    public int getNumberOfDeletedAlerts() {
        return numberOfDeletedAlerts;
    }

    /**
     * Sets number of deleted alerts.
     * 
     * @param numberOfDeletedAlerts
     */
    public void setNumberOfDeletedAlerts(int numberOfDeletedAlerts) {
        this.numberOfDeletedAlerts = numberOfDeletedAlerts;
    }

    /**
     * Returns average number of alerts per user.
     * 
     * @return averageNumberOfAlertsPerUser
     */
    public BigDecimal getAverageNumberOfAlertsPerUser() {
        return averageNumberOfAlertsPerUser;
    }

    /**
     * Sets average number of alerts per user.
     * 
     * @param averageNumberOfAlertsPerUser
     */
    public void setAverageNumberOfAlertsPerUser(BigDecimal averageNumberOfAlertsPerUser) {
        this.averageNumberOfAlertsPerUser = averageNumberOfAlertsPerUser;
    }

}
