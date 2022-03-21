package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.ps.service.report.noticereports.reporthandler.AlertsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Provided data for Alerts Control Report.
 * 
 */
public class AlertsReportData extends ReportData {

    private static final long serialVersionUID = -5556173939674116802L;

    public static final String REPORT_NAME = "Alerts as of";

    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

    public static final String FILTER_FROM_DATE = "fromDate";

    public static final String FILTER_TO_DATE = "toDate";

    public static final String MONTHLY_FREQUENCY = "Monthly";

    public static final String MONTHLY_FREQUENCY_CODE = "MN";

    public static final String QUARTERLY_FREQUENCY = "Quarterly";

    public static final String QUARTERLY_FREQUENCY_CODE = "QR";

    public static final String SEMI_ANNUALLY_FREQUENCY = "Semi-annually";

    public static final String SEMI_ANNUALLY_FREQUENCY_CODE = "SA";

    public static final String ANNUALLY_FREQUENCY = "Annually";

    public static final String ANNUALLY_FREQUENCY_CODE = "AN";

    public static final String ADHOC_FREQUENCY = "Adhoc";

    public static final String ADHOC_FREQUENCY_CODE = "AH";

    private List<AlertsReportUserStatsVO> alertUsersStatsList;

    private List<AlertsReportFreqVO> alertFrequencyStatsList;

    private List<AlertsReportDistributionVO> alertMonthlyDistributionList;
   
    public FastDateFormat format = FastDateFormat.getInstance("MMM dd, yyyy");
    
    private Integer contractNo;

    private Date fromDate;

    private Date toDate;

    /**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return format.format(Calendar.getInstance().getTime());
	}

	/**
     * Constructor
     * 
     * @param criteria
     * @param totalCount
     */
    public AlertsReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);

    }

    /**
     * Default Constructor.
     * 
     */
    public AlertsReportData() {
        super(new ReportCriteria(AlertsReportHandler.REPORT_ID), 0);

    }

    /**
     * Returns Alert Users Statistics list.
     * 
     * @return alertUsersStatsList
     */
    public List<AlertsReportUserStatsVO> getAlertUsersStatsList() {
        return alertUsersStatsList;

    }

    /**
     * Sets alert users statistics list.
     * 
     * @param alertUsersStatsList
     */
    public void setAlertUsersStatsList(List<AlertsReportUserStatsVO> alertUsersStatsList) {
        this.alertUsersStatsList = alertUsersStatsList;
    }

    /**
     * Gets Alert Frequency Statistics List
     * 
     * @return alertFrequencyStatsList
     */
    public List<AlertsReportFreqVO> getAlertFrequencyStatsList() {
        return alertFrequencyStatsList;
    }

    /**
     * Sets Alert Frequency Statistics List.
     * 
     * @param alertFrequencyStatsList
     */
    public void setAlertFrequencyStatsList(List<AlertsReportFreqVO> alertFrequencyStatsList) {
        this.alertFrequencyStatsList = alertFrequencyStatsList;
    }

    /**
     * Gets alerts monthly distribution list.
     * 
     * @return alertMonthlyDistributionList.
     */
    public List<AlertsReportDistributionVO> getAlertMonthlyDistributionList() {
        return alertMonthlyDistributionList;
    }

    /**
     * Gets alerts monthly distribution list.
     * 
     * @param alertMonthlyDistributionList
     */
    public void setAlertMonthlyDistributionList(
            List<AlertsReportDistributionVO> alertMonthlyDistributionList) {
        this.alertMonthlyDistributionList = alertMonthlyDistributionList;
    }

    /**
     * Returns the fromDate.
     * 
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Sets the fromDate.
     * 
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Returns the toDate.
     * 
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * Sets the toDate.
     * 
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Returns the contract number.
     * 
     * @return the contractNo
     */
    public Integer getContractNo() {
        return contractNo;
    }

    /**
     * Sets the contract number.
     * 
     * @param contractNo the contractNo to set
     */
    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

}
