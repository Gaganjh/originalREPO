package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.PlanSponsorWebsiteReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Provides report data for Notice Manager Plan Sponsor WebSite Report.
 * 
 */
public class PlanSponsorWebsiteReportData extends ReportData {

    private static final long serialVersionUID = -6325657860816336868L;

    public static final String REPORT_NAME = "Plan Sponsor Website as of";

    /** The filter parameter for a String object contract number */
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

    /** The filter parameter for the fromDate */
    public static final String FILTER_FROM_DATE = "fromDate";

    /** The filter parameter for the toDate */
    public static final String FILTER_TO_DATE = "toDate";

    /** The filter parameter for the task */
    public static final String FILTER_TASK = "task";

    public static final String TASK_PRINT = "print";

    private Integer contractNo;

    private Date fromDate;

    private Date toDate;

    private List<PlanSponsorWebsiteReportVisitorsStatsVO> visitorsUsageList;

    private PlanSponsorWebsiteReportMonthVisitsVO monthWithMostVisits;

    private List<PlanSponsorWebsiteReportPagesVisitedVO> pagesVisitedList;
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public FastDateFormat format = FastDateFormat.getInstance("MMM dd, yyyy");
    
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
    public PlanSponsorWebsiteReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);

    }

    public PlanSponsorWebsiteReportData() {
        super(new ReportCriteria(PlanSponsorWebsiteReportHandler.REPORT_ID), 0);

    }

    /**
     * @return the contractNo
     */
    public Integer getContractNo() {
        return contractNo;
    }

    /**
     * @param contractNo the contractNo to set
     */
    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Gets Visitors Usage List.
     * 
     * @return visitorsUsageList
     */
    public List<PlanSponsorWebsiteReportVisitorsStatsVO> getVisitorsUsageList() {
        return visitorsUsageList;
    }

    /**
     * Sets Visitor Usage List.
     * 
     * @param visitorsUsageList
     */
    public void setVisitorsUsageList(List<PlanSponsorWebsiteReportVisitorsStatsVO> visitorsUsageList) {
        this.visitorsUsageList = visitorsUsageList;
    }

    /**
     * Gets month with most visits.
     * 
     * @return
     */
    public PlanSponsorWebsiteReportMonthVisitsVO getMonthWithMostVisits() {
        return monthWithMostVisits;
    }

    /**
     * Sets month with most visits.
     * 
     * @param monthWithMostVisits
     */
    public void setMonthWithMostVisits(PlanSponsorWebsiteReportMonthVisitsVO monthWithMostVisits) {
        this.monthWithMostVisits = monthWithMostVisits;
    }

    /**
     * Get Pages visited list.
     * 
     * @return pagesVisitedList
     */
    public List<PlanSponsorWebsiteReportPagesVisitedVO> getPagesVisitedList() {
        return pagesVisitedList;
    }

    /**
     * Set Pages Visited List.
     * 
     * @param pagesVisitedList
     */
    public void setPagesVisitedList(List<PlanSponsorWebsiteReportPagesVisitedVO> pagesVisitedList) {
        this.pagesVisitedList = pagesVisitedList;
    }

}
